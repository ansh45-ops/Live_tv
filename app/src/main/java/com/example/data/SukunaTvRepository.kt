package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

data class StreamPingResult(
    val url: String,
    val isAlive: Boolean,
    val statusCode: Int,
    val latencyMs: Long,
    val contentType: String
)

class SukunaTvRepository(private val dao: SukunaTvDao) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    val favoriteEntities: Flow<List<FavoriteChannelEntity>> = dao.getAllFavorites()
    val recentHistory: Flow<List<WatchHistoryEntity>> = dao.getRecentHistory()
    val customChannelEntities: Flow<List<CustomChannelEntity>> = dao.getAllCustomChannels()
    val logoOverrides: Flow<List<ChannelLogoOverrideEntity>> = dao.getAllLogoOverrides()

    suspend fun toggleFavorite(channelId: Int, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            dao.removeFavorite(channelId)
        } else {
            dao.insertFavorite(FavoriteChannelEntity(channelId = channelId))
        }
    }

    suspend fun recordWatchHistory(channel: TvChannel, streamUrl: String) {
        dao.insertWatchHistory(
            WatchHistoryEntity(
                channelId = channel.id,
                channelName = channel.name,
                countryCode = channel.countryCode,
                streamUrl = streamUrl,
                watchedAt = System.currentTimeMillis()
            )
        )
    }

    /**
     * Copies a user-picked image file from the Android Photo Picker into local `filesDir/logo/<slug>.png`
     * and persists the override in Room so the channel logo updates immediately across the entire app.
     */
    suspend fun updateChannelLogoFromFileUri(
        context: Context,
        channel: TvChannel,
        sourceUri: Uri
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val logoDir = File(context.filesDir, "logo")
            if (!logoDir.exists()) {
                logoDir.mkdirs()
            }
            val fileName = "${channelLogoSlug(channel.name)}_${System.currentTimeMillis()}.png"
            val destFile = File(logoDir, fileName)

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return@withContext Result.failure(IllegalStateException("Unable to read selected image file"))

            val localFileUri = Uri.fromFile(destFile).toString()
            dao.insertLogoOverride(
                ChannelLogoOverrideEntity(
                    channelId = channel.id,
                    channelName = channel.name,
                    customLogoUri = localFileUri
                )
            )
            Result.success(localFileUri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateChannelLogoWithUrlOrPath(channel: TvChannel, customUri: String) {
        val clean = customUri.trim()
        if (clean.isEmpty()) {
            dao.deleteLogoOverride(channel.id)
        } else {
            dao.insertLogoOverride(
                ChannelLogoOverrideEntity(
                    channelId = channel.id,
                    channelName = channel.name,
                    customLogoUri = clean
                )
            )
        }
    }

    suspend fun resetChannelLogoToDefault(channelId: Int) {
        dao.deleteLogoOverride(channelId)
    }

    suspend fun resetAllChannelLogos() {
        dao.clearAllLogoOverrides()
    }

    suspend fun addCustomChannel(
        name: String,
        country: String,
        countryCode: String,
        category: ChannelCategory,
        streamUrl: String,
        logoUrl: String? = null,
        apiOrigin: String = "User Custom Stream"
    ) {
        dao.insertCustomChannel(
            CustomChannelEntity(
                name = name.trim(),
                country = country.trim().ifEmpty { "Global" },
                countryCode = countryCode.trim().lowercase().ifEmpty { "un" },
                categoryName = category.name,
                logoUrl = logoUrl?.trim()?.takeIf { it.isNotEmpty() },
                streamUrl = streamUrl.trim(),
                apiOrigin = apiOrigin
            )
        )
    }

    suspend fun deleteCustomChannel(customDbId: Int) {
        dao.deleteCustomChannel(customDbId)
    }

    suspend fun pingStreamUrl(url: String): StreamPingResult = withContext(Dispatchers.IO) {
        val start = System.currentTimeMillis()
        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) SukunaTV/2.0 ExoPlayerLib/2.19")
                .get()
                .build()
            httpClient.newCall(request).execute().use { response ->
                val elapsed = System.currentTimeMillis() - start
                val code = response.code
                val contentType = response.header("Content-Type") ?: "application/vnd.apple.mpegurl"
                val isAlive = code in 200..299
                StreamPingResult(
                    url = url,
                    isAlive = isAlive,
                    statusCode = code,
                    latencyMs = elapsed,
                    contentType = contentType
                )
            }
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - start
            StreamPingResult(
                url = url,
                isAlive = false,
                statusCode = -1,
                latencyMs = elapsed,
                contentType = e.localizedMessage ?: "Connection Error"
            )
        }
    }

    /**
     * Fetches and parses either an M3U/M3U8 playlist or FFxAPIs/IcX JSON schema from any URL.
     * Stores up to [maxLimit] valid HLS streams into Room so the user can watch them right away.
     */
    suspend fun syncFromApiUrl(
        endpointUrl: String,
        providerName: String,
        defaultCountry: String = "Global",
        defaultCountryCode: String = "un",
        maxLimit: Int = 80
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(endpointUrl.trim())
                .header("User-Agent", "SukunaTV/2.0 (Android; IPTV Engine)")
                .get()
                .build()

            val bodyText = httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        IllegalStateException("HTTP ${response.code} from API endpoint")
                    )
                }
                response.body?.string().orEmpty()
            }

            if (bodyText.isBlank()) {
                return@withContext Result.failure(IllegalStateException("Empty API response"))
            }

            val parsedEntities = if (bodyText.trimStart().startsWith("{") || bodyText.trimStart().startsWith("[")) {
                parseJsonChannelPayload(bodyText, providerName, defaultCountry, defaultCountryCode, maxLimit)
            } else {
                parseM3uPlaylist(bodyText, providerName, defaultCountry, defaultCountryCode, maxLimit)
            }

            if (parsedEntities.isEmpty()) {
                return@withContext Result.failure(IllegalStateException("No playable .m3u8 streams found in response"))
            }

            dao.deleteChannelsByOrigin(providerName)
            dao.insertCustomChannels(parsedEntities)
            Result.success(parsedEntities.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun parseRawTextAndSave(
        rawText: String,
        originName: String = "Imported Payload"
    ): List<CustomChannelEntity> {
        val trimmed = rawText.trim()
        return if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            parseJsonChannelPayload(trimmed, originName, "Global", "un", 100)
        } else {
            parseM3uPlaylist(trimmed, originName, "Global", "un", 100)
        }
    }

    suspend fun saveImportedEntities(entities: List<CustomChannelEntity>) {
        if (entities.isNotEmpty()) {
            dao.insertCustomChannels(entities)
        }
    }

    private fun parseM3uPlaylist(
        content: String,
        providerName: String,
        defaultCountry: String,
        defaultCountryCode: String,
        maxLimit: Int
    ): List<CustomChannelEntity> {
        val lines = content.lines()
        val result = mutableListOf<CustomChannelEntity>()

        var pendingName = ""
        var pendingLogo: String? = null
        var pendingGroup = ""
        var pendingCountry = defaultCountry
        var pendingCountryCode = defaultCountryCode

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isEmpty()) continue

            if (line.startsWith("#EXTINF:", ignoreCase = true)) {
                val commaIndex = line.lastIndexOf(',')
                pendingName = if (commaIndex != -1 && commaIndex + 1 < line.length) {
                    line.substring(commaIndex + 1).trim()
                } else {
                    "Live Channel"
                }

                pendingLogo = extractAttribute(line, "tvg-logo")
                pendingGroup = extractAttribute(line, "group-title").orEmpty()
                val tvgCountry = extractAttribute(line, "tvg-country")
                if (!tvgCountry.isNullOrBlank()) {
                    pendingCountryCode = tvgCountry.lowercase().take(2)
                    pendingCountry = countryNameFromCode(pendingCountryCode)
                } else {
                    val inferredCode = inferCountryCode(providerName, pendingName)
                    pendingCountryCode = inferredCode
                    pendingCountry = countryNameFromCode(inferredCode)
                }
            } else if (!line.startsWith("#") && (line.startsWith("http://") || line.startsWith("https://"))) {
                val cleanName = pendingName.ifBlank {
                    line.substringBeforeLast('/').substringAfterLast('/').ifBlank { "Live Stream" }
                }
                val category = inferCategory(cleanName, pendingGroup, providerName)
                result.add(
                    CustomChannelEntity(
                        name = cleanName.take(48),
                        country = pendingCountry,
                        countryCode = pendingCountryCode,
                        categoryName = category.name,
                        logoUrl = pendingLogo,
                        streamUrl = line,
                        apiOrigin = providerName
                    )
                )
                pendingName = ""
                pendingLogo = null
                pendingGroup = ""
                if (result.size >= maxLimit) break
            }
        }
        return result
    }

    private fun parseJsonChannelPayload(
        jsonString: String,
        providerName: String,
        defaultCountry: String,
        defaultCountryCode: String,
        maxLimit: Int
    ): List<CustomChannelEntity> {
        val result = mutableListOf<CustomChannelEntity>()
        try {
            val channelsArray: JSONArray = if (jsonString.trimStart().startsWith("{")) {
                val root = JSONObject(jsonString)
                root.optJSONArray("channels") ?: JSONArray()
            } else {
                JSONArray(jsonString)
            }

            for (i in 0 until channelsArray.length()) {
                if (result.size >= maxLimit) break
                val item = channelsArray.optJSONObject(i) ?: continue
                val name = item.optString("name", "Channel #${i + 1}")
                val country = item.optString("country", defaultCountry).ifBlank { defaultCountry }
                val countryCode = item.optString("country_code", defaultCountryCode)
                    .ifBlank { defaultCountryCode }
                    .lowercase()
                val logoUrl = if (item.isNull("logo_url")) null else item.optString("logo_url")

                val streamsArr = item.optJSONArray("streams")
                var streamUrl = item.optString("url", "")
                if (streamUrl.isBlank() && streamsArr != null && streamsArr.length() > 0) {
                    val firstStream = streamsArr.optJSONObject(0)
                    streamUrl = firstStream?.optString("url", "").orEmpty()
                }

                if (streamUrl.startsWith("http")) {
                    val category = inferCategory(name, "", providerName)
                    result.add(
                        CustomChannelEntity(
                            name = name,
                            country = country,
                            countryCode = countryCode,
                            categoryName = category.name,
                            logoUrl = logoUrl?.takeIf { it.isNotBlank() && it != "null" },
                            streamUrl = streamUrl,
                            apiOrigin = providerName
                        )
                    )
                }
            }
        } catch (_: Exception) {
        }
        return result
    }

    private fun extractAttribute(extInfLine: String, key: String): String? {
        val token = "$key=\""
        val startIdx = extInfLine.indexOf(token, ignoreCase = true)
        if (startIdx == -1) return null
        val valueStart = startIdx + token.length
        val endIdx = extInfLine.indexOf('"', valueStart)
        if (endIdx == -1) return null
        return extInfLine.substring(valueStart, endIdx).trim().takeIf { it.isNotEmpty() }
    }

    private fun inferCountryCode(providerName: String, channelName: String): String {
        val combined = "$providerName $channelName".lowercase()
        return when {
            combined.contains("bangladesh") || combined.contains("bangla") || combined.contains("🇧🇩") -> "bd"
            combined.contains("india") || combined.contains("hindi") || combined.contains("bhojpuri") || combined.contains("🇮🇳") -> "in"
            combined.contains("japan") || combined.contains("nhk") || combined.contains("anime") -> "jp"
            combined.contains("korea") || combined.contains("kbs") || combined.contains("arirang") -> "kr"
            combined.contains("uk") || combined.contains("bbc") || combined.contains("sky") -> "gb"
            combined.contains("usa") || combined.contains("united states") -> "us"
            else -> "un"
        }
    }

    private fun countryNameFromCode(code: String): String = when (code.lowercase()) {
        "bd" -> "Bangladesh"
        "in" -> "India"
        "us" -> "United States"
        "gb", "uk" -> "United Kingdom"
        "jp" -> "Japan"
        "kr" -> "South Korea"
        "qa" -> "Qatar"
        "de" -> "Germany"
        "fr" -> "France"
        "sg" -> "Singapore"
        "tr" -> "Turkey"
        else -> "Global"
    }

    private fun inferCategory(name: String, group: String, provider: String): ChannelCategory {
        val text = "$name $group $provider".lowercase()
        return when {
            text.contains("sport") || text.contains("cricket") || text.contains("football") -> ChannelCategory.SPORTS
            text.contains("music") || text.contains("9xm") || text.contains("jalwa") || text.contains("beats") || text.contains("dhoom") -> ChannelCategory.MUSIC
            text.contains("movie") || text.contains("cinema") || text.contains("goldmines") || text.contains("filamchi") || text.contains("kadak") -> ChannelCategory.MOVIES
            text.contains("kid") || text.contains("anime") || text.contains("animation") || text.contains("cartoon") || text.contains("duronto") || text.contains("gubbare") -> ChannelCategory.KIDS
            text.contains("news") || text.contains("aaj tak") || text.contains("abp") || text.contains("somoy") || text.contains("jamuna") || text.contains("ndtv") || text.contains("cnbc") || text.contains("wion") -> ChannelCategory.NEWS
            text.contains("peace") || text.contains("madani") || text.contains("islam") || text.contains("quran") || text.contains("bhakti") -> ChannelCategory.RELIGIOUS
            text.contains("food") || text.contains("travel") || text.contains("lifestyle") -> ChannelCategory.LIFESTYLE
            text.contains("nasa") || text.contains("science") || text.contains("doc") -> ChannelCategory.SCIENCE
            else -> ChannelCategory.ENTERTAINMENT
        }
    }
}

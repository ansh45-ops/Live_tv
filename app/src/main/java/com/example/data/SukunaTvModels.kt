package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

enum class ChannelCategory(val label: String, val badgeText: String) {
    ALL("All Channels", "ALL"),
    NEWS("News & Live", "NEWS"),
    ENTERTAINMENT("General & Drama", "ENT"),
    MOVIES("Cinema & Action TV", "CINEMA"),
    MUSIC("Music & Beats", "MUS"),
    SPORTS("Sports Arena", "SPT"),
    KIDS("Kids & Anime", "KIDS"),
    LIFESTYLE("Food & Culture", "LIFE"),
    RELIGIOUS("Spiritual", "REL"),
    SCIENCE("Sci-Tech & Doc", "SCI")
}

data class StreamSource(
    val id: Int,
    val url: String,
    val format: String = "hls",
    val extension: String = "m3u8",
    val source: String = "FFxAPIs/IcX",
    val verified: Boolean = true,
    val label: String = "Server #1"
)

fun channelLogoSlug(name: String): String {
    return name.map { if (it.isLetterOrDigit()) it.lowercaseChar() else '_' }
        .joinToString("")
        .trim('_')
        .replace(Regex("_+"), "_")
}

data class TvChannel(
    val id: Int,
    val name: String,
    val country: String,
    val countryCode: String,
    val category: ChannelCategory,
    val logoUrl: String? = null,
    val streams: List<StreamSource>,
    val isCustom: Boolean = false,
    val apiOrigin: String = "FFxAPIs/IcX Core"
) {
    val logoAssetFileName: String
        get() = "${channelLogoSlug(name)}.png"

    val defaultAssetLogoUri: String
        get() = "file:///android_asset/logo/$logoAssetFileName"

    /**
     * Resolves the active logo URI:
     * 1. User-customized file override or remote URL (if set in logoUrl)
     * 2. Dedicated local file in app/src/main/assets/logo/<channel_slug>.png
     */
    val effectiveLogoUri: String
        get() = logoUrl?.takeIf { it.isNotBlank() } ?: defaultAssetLogoUri

    val flagEmoji: String
        get() = when (countryCode.lowercase()) {
            "bd" -> "🇧🇩"
            "in" -> "🇮🇳"
            "us" -> "🇺🇸"
            "gb", "uk" -> "🇬🇧"
            "jp" -> "🇯🇵"
            "kr" -> "🇰🇷"
            "qa" -> "🇶🇦"
            "fr" -> "🇫🇷"
            "de" -> "🇩🇪"
            "sg" -> "🇸🇬"
            "ae" -> "🇦🇪"
            "ca" -> "🇨🇦"
            "tr" -> "🇹🇷"
            "es" -> "🇪🇸"
            else -> "🌐"
        }
}

data class FreeApiProvider(
    val id: String,
    val name: String,
    val description: String,
    val endpointUrl: String,
    val regionTag: String,
    val format: String,
    val estimatedChannels: String,
    val isPreloaded: Boolean = false
)

@Entity(tableName = "favorite_channels")
data class FavoriteChannelEntity(
    @PrimaryKey val channelId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val channelId: Int,
    val channelName: String,
    val countryCode: String,
    val streamUrl: String,
    val watchedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_channels")
data class CustomChannelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val countryCode: String,
    val categoryName: String,
    val logoUrl: String?,
    val streamUrl: String,
    val apiOrigin: String = "Custom / Imported API",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "channel_logo_overrides")
data class ChannelLogoOverrideEntity(
    @PrimaryKey val channelId: Int,
    val channelName: String,
    val customLogoUri: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Dao
interface SukunaTvDao {
    @Query("SELECT * FROM favorite_channels ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoriteChannelEntity)

    @Query("DELETE FROM favorite_channels WHERE channelId = :channelId")
    suspend fun removeFavorite(channelId: Int)

    @Query("SELECT * FROM watch_history ORDER BY watchedAt DESC LIMIT 25")
    fun getRecentHistory(): Flow<List<WatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchHistory(entity: WatchHistoryEntity)

    @Query("SELECT * FROM custom_channels ORDER BY createdAt DESC")
    fun getAllCustomChannels(): Flow<List<CustomChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomChannel(entity: CustomChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomChannels(entities: List<CustomChannelEntity>)

    @Query("DELETE FROM custom_channels WHERE id = :id")
    suspend fun deleteCustomChannel(id: Int)

    @Query("DELETE FROM custom_channels WHERE apiOrigin = :origin")
    suspend fun deleteChannelsByOrigin(origin: String)

    @Query("SELECT * FROM channel_logo_overrides ORDER BY updatedAt DESC")
    fun getAllLogoOverrides(): Flow<List<ChannelLogoOverrideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogoOverride(entity: ChannelLogoOverrideEntity)

    @Query("DELETE FROM channel_logo_overrides WHERE channelId = :channelId")
    suspend fun deleteLogoOverride(channelId: Int)

    @Query("DELETE FROM channel_logo_overrides")
    suspend fun clearAllLogoOverrides()
}

@Database(
    entities = [
        FavoriteChannelEntity::class,
        WatchHistoryEntity::class,
        CustomChannelEntity::class,
        ChannelLogoOverrideEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SukunaTvDatabase : RoomDatabase() {
    abstract fun dao(): SukunaTvDao
}

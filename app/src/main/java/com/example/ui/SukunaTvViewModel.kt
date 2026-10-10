package com.example.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.BuiltInChannelsCatalog
import com.example.data.ChannelCategory
import com.example.data.ChannelLogoOverrideEntity
import com.example.data.FreeApiProvider
import com.example.data.StreamPingResult
import com.example.data.StreamSource
import com.example.data.SukunaTvRepository
import com.example.data.TvChannel
import com.example.data.WatchHistoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SukunaTab(val title: String) {
    HOME("Home"),
    LIVE_TV("Live Grid"),
    LOGO_STUDIO("Logos"),
    API_HUB("Free APIs"),
    FAVORITES("My List")
}

enum class HomeFeedFilter(val label: String) {
    ALL("All Live"),
    BANGLADESH("🇧🇩 Bangladesh"),
    INDIA("🇮🇳 India"),
    SPORTS_NEWS("Sports & News"),
    ANIME_MUSIC("Anime & Music")
}

data class SukunaUiState(
    val showOpeningSplash: Boolean = true,
    val homeFeedFilter: HomeFeedFilter = HomeFeedFilter.ALL,
    val allChannels: List<TvChannel> = BuiltInChannelsCatalog.initialChannels,
    val filteredChannels: List<TvChannel> = BuiltInChannelsCatalog.initialChannels,
    val selectedChannel: TvChannel = BuiltInChannelsCatalog.initialChannels.first(),
    val selectedStreamIndex: Int = 0,
    val isPlaying: Boolean = true,
    val isBuffering: Boolean = false,
    val playerError: String? = null,
    val isFullscreen: Boolean = false,
    val searchQuery: String = "",
    val selectedCountryCode: String = "ALL",
    val selectedCategory: ChannelCategory = ChannelCategory.ALL,
    val activeTab: SukunaTab = SukunaTab.HOME,
    val favoriteIds: Set<Int> = emptySet(),
    val recentHistory: List<WatchHistoryEntity> = emptyList(),
    val logoOverridesMap: Map<Int, String> = emptyMap(),
    val freeApis: List<FreeApiProvider> = BuiltInChannelsCatalog.freeApiProviders,
    val syncingApiId: String? = null,
    val syncedApiCounts: Map<String, Int> = mapOf("ffxapis_icx" to 41),
    val statusBannerMessage: String? = null,
    val pingResults: Map<String, StreamPingResult> = emptyMap(),
    val isPingingCurrent: Boolean = false,
    val showAddStreamDialog: Boolean = false,
    val domainExpansionActive: Boolean = true
) {
    val currentStream: StreamSource
        get() = selectedChannel.streams.getOrElse(selectedStreamIndex) {
            selectedChannel.streams.first()
        }
}

private data class FilterQueryState(
    val searchQuery: String = "",
    val countryCode: String = "ALL",
    val category: ChannelCategory = ChannelCategory.ALL,
    val activeTab: SukunaTab = SukunaTab.HOME,
    val homeFeedFilter: HomeFeedFilter = HomeFeedFilter.ALL,
    val showOpeningSplash: Boolean = true
)

private data class PlayerControlState(
    val selectedChannel: TvChannel = BuiltInChannelsCatalog.initialChannels.first(),
    val selectedStreamIndex: Int = 0,
    val isPlaying: Boolean = true,
    val isBuffering: Boolean = false,
    val playerError: String? = null,
    val isFullscreen: Boolean = false,
    val domainExpansionActive: Boolean = true
)

private data class ApiLabState(
    val syncingApiId: String? = null,
    val syncedApiCounts: Map<String, Int> = mapOf("ffxapis_icx" to 41),
    val statusBannerMessage: String? = null,
    val pingResults: Map<String, StreamPingResult> = emptyMap(),
    val isPingingCurrent: Boolean = false,
    val showAddStreamDialog: Boolean = false
)

class SukunaTvViewModel(
    private val repository: SukunaTvRepository
) : ViewModel() {

    private val filterState = MutableStateFlow(FilterQueryState())
    private val playerState = MutableStateFlow(PlayerControlState())
    private val apiLabState = MutableStateFlow(ApiLabState())

    val uiState: StateFlow<SukunaUiState> = combine(
        combine(
            repository.favoriteEntities,
            repository.recentHistory,
            repository.customChannelEntities,
            repository.logoOverrides
        ) { favs, hist, custom, logos ->
            Quadruple(favs, hist, custom, logos)
        },
        filterState,
        playerState,
        apiLabState
    ) { (favorites, history, customEntities, logoOverridesList), filter, player, apiLab ->

        val favSet = favorites.map { it.channelId }.toSet()
        val logoMap: Map<Int, String> = logoOverridesList.associate { it.channelId to it.customLogoUri }

        val customMapped = customEntities.map { entity ->
            val category = runCatching { ChannelCategory.valueOf(entity.categoryName) }
                .getOrDefault(ChannelCategory.ENTERTAINMENT)
            val syntheticId = 10_000 + entity.id
            TvChannel(
                id = syntheticId,
                name = entity.name,
                country = entity.country,
                countryCode = entity.countryCode,
                category = category,
                logoUrl = logoMap[syntheticId] ?: entity.logoUrl,
                streams = listOf(
                    StreamSource(
                        id = syntheticId,
                        url = entity.streamUrl,
                        source = entity.apiOrigin,
                        verified = true,
                        label = "${entity.apiOrigin} Live"
                    )
                ),
                isCustom = true,
                apiOrigin = entity.apiOrigin
            )
        }

        val builtInWithLogos = BuiltInChannelsCatalog.initialChannels.map { ch ->
            val overrideUri = logoMap[ch.id]
            if (overrideUri != null) ch.copy(logoUrl = overrideUri) else ch
        }

        val combinedChannels = builtInWithLogos + customMapped

        val filtered = combinedChannels.filter { ch ->
            val matchesCountry = filter.countryCode == "ALL" ||
                ch.countryCode.equals(filter.countryCode, ignoreCase = true)

            val matchesCategory = filter.category == ChannelCategory.ALL ||
                ch.category == filter.category

            val q = filter.searchQuery.trim().lowercase()
            val matchesSearch = q.isEmpty() ||
                ch.name.lowercase().contains(q) ||
                ch.country.lowercase().contains(q) ||
                ch.category.label.lowercase().contains(q) ||
                ch.logoAssetFileName.lowercase().contains(q) ||
                ch.apiOrigin.lowercase().contains(q)

            matchesCountry && matchesCategory && matchesSearch
        }

        val activeChannel = combinedChannels.find { it.id == player.selectedChannel.id }
            ?: player.selectedChannel

        SukunaUiState(
            showOpeningSplash = filter.showOpeningSplash,
            homeFeedFilter = filter.homeFeedFilter,
            allChannels = combinedChannels,
            filteredChannels = filtered,
            selectedChannel = activeChannel,
            selectedStreamIndex = player.selectedStreamIndex.coerceIn(
                0,
                (activeChannel.streams.size - 1).coerceAtLeast(0)
            ),
            isPlaying = player.isPlaying,
            isBuffering = player.isBuffering,
            playerError = player.playerError,
            isFullscreen = player.isFullscreen,
            searchQuery = filter.searchQuery,
            selectedCountryCode = filter.countryCode,
            selectedCategory = filter.category,
            activeTab = filter.activeTab,
            favoriteIds = favSet,
            recentHistory = history,
            logoOverridesMap = logoMap,
            freeApis = BuiltInChannelsCatalog.freeApiProviders,
            syncingApiId = apiLab.syncingApiId,
            syncedApiCounts = apiLab.syncedApiCounts,
            statusBannerMessage = apiLab.statusBannerMessage,
            pingResults = apiLab.pingResults,
            isPingingCurrent = apiLab.isPingingCurrent,
            showAddStreamDialog = apiLab.showAddStreamDialog,
            domainExpansionActive = player.domainExpansionActive
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SukunaUiState()
    )

    init {
        val firstChannel = BuiltInChannelsCatalog.initialChannels.first()
        pingStream(firstChannel.streams.first().url)
    }

    fun finishOpeningSplash() {
        filterState.update { it.copy(showOpeningSplash = false) }
    }

    fun replayOpeningSplash() {
        filterState.update { it.copy(showOpeningSplash = true) }
    }

    fun setHomeFeedFilter(filter: HomeFeedFilter) {
        filterState.update { it.copy(homeFeedFilter = filter) }
    }

    fun selectChannel(channel: TvChannel, streamIdx: Int = 0, navigateToPlayerTab: Boolean = false) {
        val safeIdx = streamIdx.coerceIn(0, (channel.streams.size - 1).coerceAtLeast(0))
        playerState.update {
            it.copy(
                selectedChannel = channel,
                selectedStreamIndex = safeIdx,
                isPlaying = true,
                playerError = null
            )
        }
        if (navigateToPlayerTab) {
            filterState.update { it.copy(activeTab = SukunaTab.LIVE_TV) }
        }
        val streamUrl = channel.streams.getOrElse(safeIdx) { channel.streams.first() }.url
        viewModelScope.launch {
            repository.recordWatchHistory(channel, streamUrl)
        }
        pingStream(streamUrl)
    }

    fun updateChannelLogoFromFile(context: Context, channel: TvChannel, uri: Uri) {
        viewModelScope.launch {
            val res = repository.updateChannelLogoFromFileUri(context, channel, uri)
            res.fold(
                onSuccess = {
                    showBanner("Updated logo file for '${channel.name}'!")
                },
                onFailure = { err ->
                    showBanner("Could not update logo: ${err.localizedMessage ?: "File error"}")
                }
            )
        }
    }

    fun updateChannelLogoWithCustomPath(channel: TvChannel, customUri: String) {
        viewModelScope.launch {
            repository.updateChannelLogoWithUrlOrPath(channel, customUri)
            showBanner("Saved custom logo path for '${channel.name}'!")
        }
    }

    fun resetChannelLogo(channel: TvChannel) {
        viewModelScope.launch {
            repository.resetChannelLogoToDefault(channel.id)
            showBanner("Restored default assets/logo/${channel.logoAssetFileName} for '${channel.name}'")
        }
    }

    fun resetAllChannelLogos() {
        viewModelScope.launch {
            repository.resetAllChannelLogos()
            showBanner("Restored all channel logos to default assets/logo/ files!")
        }
    }

    fun selectStreamServer(index: Int) {
        val currentChannel = uiState.value.selectedChannel
        val safeIdx = index.coerceIn(0, (currentChannel.streams.size - 1).coerceAtLeast(0))
        playerState.update {
            it.copy(
                selectedStreamIndex = safeIdx,
                isPlaying = true,
                playerError = null
            )
        }
        val streamUrl = currentChannel.streams[safeIdx].url
        pingStream(streamUrl)
    }

    fun tryNextServerOrChannelOnError(errorMessage: String) {
        val currentChannel = uiState.value.selectedChannel
        val currentIndex = uiState.value.selectedStreamIndex
        if (currentIndex + 1 < currentChannel.streams.size) {
            val nextIdx = currentIndex + 1
            playerState.update {
                it.copy(
                    selectedStreamIndex = nextIdx,
                    playerError = null,
                    isPlaying = true
                )
            }
            showBanner("Switched to backup server #${nextIdx + 1} for ${currentChannel.name}")
        } else {
            playerState.update {
                it.copy(
                    playerError = errorMessage,
                    isBuffering = false
                )
            }
        }
    }

    fun setPlaying(playing: Boolean) {
        playerState.update { it.copy(isPlaying = playing) }
    }

    fun setBuffering(buffering: Boolean) {
        playerState.update { it.copy(isBuffering = buffering) }
    }

    fun clearPlayerError() {
        playerState.update { it.copy(playerError = null) }
    }

    fun toggleFullscreen() {
        playerState.update { it.copy(isFullscreen = !it.isFullscreen) }
    }

    fun setFullscreen(fullscreen: Boolean) {
        playerState.update { it.copy(isFullscreen = fullscreen) }
    }

    fun toggleDomainExpansionAura() {
        playerState.update { it.copy(domainExpansionActive = !it.domainExpansionActive) }
    }

    fun setSearchQuery(query: String) {
        filterState.update { it.copy(searchQuery = query) }
    }

    fun setCountryFilter(countryCode: String) {
        filterState.update { it.copy(countryCode = countryCode) }
    }

    fun setCategoryFilter(category: ChannelCategory) {
        filterState.update { it.copy(category = category) }
    }

    fun setActiveTab(tab: SukunaTab) {
        filterState.update { it.copy(activeTab = tab) }
    }

    fun toggleFavorite(channelId: Int) {
        val isFav = uiState.value.favoriteIds.contains(channelId)
        viewModelScope.launch {
            repository.toggleFavorite(channelId, isFav)
        }
    }

    fun setShowAddStreamDialog(show: Boolean) {
        apiLabState.update { it.copy(showAddStreamDialog = show) }
    }

    fun addCustomStream(
        name: String,
        country: String,
        countryCode: String,
        category: ChannelCategory,
        streamUrl: String
    ) {
        if (name.isBlank() || streamUrl.isBlank()) return
        viewModelScope.launch {
            repository.addCustomChannel(
                name = name,
                country = country,
                countryCode = countryCode,
                category = category,
                streamUrl = streamUrl
            )
            apiLabState.update {
                it.copy(
                    showAddStreamDialog = false,
                    statusBannerMessage = "Added '$name' to Sukuna TV Live Directory!"
                )
            }
        }
    }

    fun importRawPayload(rawText: String, sourceLabel: String) {
        if (rawText.isBlank()) return
        viewModelScope.launch {
            val parsed = repository.parseRawTextAndSave(rawText, sourceLabel.ifBlank { "Custom Payload" })
            if (parsed.isNotEmpty()) {
                repository.saveImportedEntities(parsed)
                showBanner("Imported ${parsed.size} live streams from $sourceLabel!")
            } else {
                showBanner("No valid .m3u8 URLs detected in input.")
            }
        }
    }

    fun syncFreeApiProvider(provider: FreeApiProvider) {
        if (apiLabState.value.syncingApiId != null) return
        apiLabState.update {
            it.copy(
                syncingApiId = provider.id,
                statusBannerMessage = "Syncing ${provider.name}…"
            )
        }
        viewModelScope.launch {
            val defaultCountry = when {
                provider.id.contains("_bd") -> "Bangladesh"
                provider.id.contains("_in") -> "India"
                else -> "Global"
            }
            val defaultCode = when {
                provider.id.contains("_bd") -> "bd"
                provider.id.contains("_in") -> "in"
                else -> "un"
            }
            val result = repository.syncFromApiUrl(
                endpointUrl = provider.endpointUrl,
                providerName = provider.name,
                defaultCountry = defaultCountry,
                defaultCountryCode = defaultCode,
                maxLimit = 60
            )
            result.fold(
                onSuccess = { count ->
                    apiLabState.update { state ->
                        state.copy(
                            syncingApiId = null,
                            syncedApiCounts = state.syncedApiCounts + (provider.id to count),
                            statusBannerMessage = "Synced $count live streams from ${provider.name}!"
                        )
                    }
                },
                onFailure = { err ->
                    apiLabState.update { state ->
                        state.copy(
                            syncingApiId = null,
                            statusBannerMessage = "API Sync notice: ${err.localizedMessage ?: "Check connection"}"
                        )
                    }
                }
            )
        }
    }

    fun syncCustomApiUrl(url: String, customTitle: String) {
        if (url.isBlank() || apiLabState.value.syncingApiId != null) return
        val providerTitle = customTitle.trim().ifBlank { "Custom M3U/JSON API" }
        apiLabState.update {
            it.copy(
                syncingApiId = "custom_url",
                statusBannerMessage = "Fetching streams from $providerTitle…"
            )
        }
        viewModelScope.launch {
            val result = repository.syncFromApiUrl(
                endpointUrl = url.trim(),
                providerName = providerTitle,
                maxLimit = 75
            )
            result.fold(
                onSuccess = { count ->
                    apiLabState.update { state ->
                        state.copy(
                            syncingApiId = null,
                            statusBannerMessage = "Loaded $count live channels from $providerTitle!"
                        )
                    }
                },
                onFailure = { err ->
                    apiLabState.update { state ->
                        state.copy(
                            syncingApiId = null,
                            statusBannerMessage = "Could not load URL: ${err.localizedMessage ?: "Invalid format"}"
                        )
                    }
                }
            )
        }
    }

    fun pingStream(url: String) {
        apiLabState.update { it.copy(isPingingCurrent = true) }
        viewModelScope.launch {
            val res = repository.pingStreamUrl(url)
            apiLabState.update { state ->
                state.copy(
                    isPingingCurrent = false,
                    pingResults = state.pingResults + (url to res)
                )
            }
        }
    }

    fun dismissBanner() {
        apiLabState.update { it.copy(statusBannerMessage = null) }
    }

    private fun showBanner(msg: String) {
        apiLabState.update { it.copy(statusBannerMessage = msg) }
    }

    private data class Quadruple<A, B, C, D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )

    class Factory(private val repository: SukunaTvRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SukunaTvViewModel(repository) as T
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Api
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.data.SukunaTvDatabase
import com.example.data.SukunaTvRepository
import com.example.data.TvChannel
import com.example.ui.SukunaTab
import com.example.ui.SukunaTvViewModel
import com.example.ui.SukunaUiState
import com.example.ui.components.AddCustomStreamDialog
import com.example.ui.components.AnimatedChannelGrid
import com.example.ui.components.AnimatedEqualizerBars
import com.example.ui.components.ChannelAvatarIcon
import com.example.ui.components.ChannelFilterBar
import com.example.ui.components.ChannelLogoStudioScreen
import com.example.ui.components.FavoritesAndHistoryScreen
import com.example.ui.components.FreeApiHubScreen
import com.example.ui.components.NetflixHomeScreen
import com.example.ui.components.SukunaEnergyBackground
import com.example.ui.components.SukunaLivePlayerSection
import com.example.ui.components.SukunaOpeningSplashOverlay
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SukunaCyan
import com.example.ui.theme.SukunaObsidian
import com.example.ui.theme.SukunaSurfaceBright
import com.example.ui.theme.SukunaTextSecondary
import kotlinx.coroutines.delay

private val NetflixRed = Color(0xFFE50914)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current.applicationContext
            val repository = remember(context) {
                val db = Room.databaseBuilder(
                    context,
                    SukunaTvDatabase::class.java,
                    "sukuna_tv_db"
                ).fallbackToDestructiveMigration().build()
                SukunaTvRepository(db.dao())
            }
            val viewModel: SukunaTvViewModel = viewModel(
                factory = SukunaTvViewModel.Factory(repository)
            )
            MyApplicationTheme {
                SukunaTvApp(viewModel = viewModel)
            }
        }
    }
}

private fun tabIcons(tab: SukunaTab): Pair<ImageVector, ImageVector> = when (tab) {
    SukunaTab.HOME -> Icons.Filled.Home to Icons.Outlined.Home
    SukunaTab.LIVE_TV -> Icons.Filled.LiveTv to Icons.Outlined.LiveTv
    SukunaTab.LOGO_STUDIO -> Icons.Filled.Image to Icons.Outlined.Image
    SukunaTab.API_HUB -> Icons.Filled.Api to Icons.Outlined.Api
    SukunaTab.FAVORITES -> Icons.Filled.Favorite to Icons.Outlined.FavoriteBorder
}

@Composable
fun SukunaTvApp(
    viewModel: SukunaTvViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var preselectedChannelForLogoStudio by remember { mutableStateOf<TvChannel?>(null) }

    // BackHandler for fullscreen or non-Home tabs
    BackHandler(enabled = state.isFullscreen || state.activeTab != SukunaTab.HOME) {
        if (state.isFullscreen) {
            viewModel.setFullscreen(false)
        } else {
            viewModel.setActiveTab(SukunaTab.HOME)
        }
    }

    // Auto-dismiss status banner after 4.5 seconds
    LaunchedEffect(state.statusBannerMessage) {
        if (state.statusBannerMessage != null) {
            delay(4500)
            viewModel.dismissBanner()
        }
    }

    if (state.showAddStreamDialog) {
        AddCustomStreamDialog(
            onDismiss = { viewModel.setShowAddStreamDialog(false) },
            onConfirm = { name, country, code, category, url ->
                viewModel.addCustomStream(name, country, code, category, url)
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SukunaObsidian)
            .testTag("sukuna_tv_root")
    ) {
        // Animated Anime Energy Canvas Background
        SukunaEnergyBackground(isDomainActive = state.domainExpansionActive)

        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                if (!state.isFullscreen) {
                    SukunaNetflixTopBar(
                        totalChannelsCount = state.allChannels.size,
                        onReplaySplashAnimation = viewModel::replayOpeningSplash,
                        onOpenLogoStudioClick = { viewModel.setActiveTab(SukunaTab.LOGO_STUDIO) },
                        onQuickSyncApisClick = { viewModel.setActiveTab(SukunaTab.API_HUB) },
                        onAddCustomClick = { viewModel.setShowAddStreamDialog(true) }
                    )
                }
            },
            bottomBar = {
                if (!state.isFullscreen) {
                    Column {
                        // Mini Now-Playing Dock when on Netflix HOME or LOGO STUDIO tab so user can jump into Live Player anytime
                        if (state.activeTab == SukunaTab.HOME || state.activeTab == SukunaTab.LOGO_STUDIO) {
                            NetflixMiniNowPlayingDock(
                                channel = state.selectedChannel,
                                isPlaying = state.isPlaying,
                                onExpandPlayer = { viewModel.setActiveTab(SukunaTab.LIVE_TV) }
                            )
                        }

                        NavigationBar(
                            containerColor = Color(0xFF0B0912).copy(alpha = 0.96f),
                            tonalElevation = 12.dp,
                            modifier = Modifier.testTag("sukuna_bottom_nav")
                        ) {
                            SukunaTab.entries.forEach { tab ->
                                val selected = state.activeTab == tab
                                val (filledIcon, outlinedIcon) = tabIcons(tab)
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.setActiveTab(tab) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) filledIcon else outlinedIcon,
                                            contentDescription = tab.title
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = NetflixRed,
                                        indicatorColor = NetflixRed,
                                        unselectedIconColor = SukunaTextSecondary,
                                        unselectedTextColor = SukunaTextSecondary
                                    ),
                                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val isWideTablet = maxWidth >= 720.dp && !state.isFullscreen

                if (state.isFullscreen) {
                    SukunaPlayerWrapper(
                        state = state,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (state.activeTab == SukunaTab.HOME) {
                    // Full-bleed Netflix Live TV Home Experience (Top 10 Live TV, Bangladesh Live, India Live, Sports & News)
                    NetflixHomeScreen(
                        channels = state.allChannels,
                        selectedChannel = state.selectedChannel,
                        favoriteIds = state.favoriteIds,
                        homeFeedFilter = state.homeFeedFilter,
                        onFilterChanged = viewModel::setHomeFeedFilter,
                        onSelectChannel = { channel ->
                            viewModel.selectChannel(channel, navigateToPlayerTab = true)
                        },
                        onToggleFavorite = viewModel::toggleFavorite,
                        onExploreAllChannels = { countryCode ->
                            viewModel.setCountryFilter(countryCode)
                            viewModel.setActiveTab(SukunaTab.LIVE_TV)
                        },
                        onOpenLogoStudio = { targetChannel ->
                            preselectedChannelForLogoStudio = targetChannel
                            viewModel.setActiveTab(SukunaTab.LOGO_STUDIO)
                        }
                    )
                } else if (state.activeTab == SukunaTab.LOGO_STUDIO) {
                    // Dedicated Channel Logo Folder Studio (`app/src/main/assets/logo/` + Device File Picker)
                    ChannelLogoStudioScreen(
                        allChannels = state.allChannels,
                        logoOverridesMap = state.logoOverridesMap,
                        initialTargetChannel = preselectedChannelForLogoStudio,
                        onPickLogoFileForChannel = { ch, uri ->
                            viewModel.updateChannelLogoFromFile(context, ch, uri)
                        },
                        onSaveCustomLogoPath = { ch, path ->
                            viewModel.updateChannelLogoWithCustomPath(ch, path)
                        },
                        onResetChannelLogo = viewModel::resetChannelLogo,
                        onResetAllLogos = viewModel::resetAllChannelLogos
                    )
                } else if (isWideTablet) {
                    // Canonical Two-Pane Layout for Tablets / Foldables / Landscape
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .weight(0.46f)
                                .fillMaxHeight()
                        ) {
                            SukunaPlayerWrapper(
                                state = state,
                                viewModel = viewModel
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(0.54f)
                                .fillMaxHeight()
                        ) {
                            SukunaTabContentArea(
                                state = state,
                                viewModel = viewModel,
                                onOpenLogoStudio = { ch ->
                                    preselectedChannelForLogoStudio = ch
                                    viewModel.setActiveTab(SukunaTab.LOGO_STUDIO)
                                }
                            )
                        }
                    }
                } else {
                    // Standard Handheld Portrait Layout for Live TV Grid / Free APIs / My List
                    Column(modifier = Modifier.fillMaxSize()) {
                        SukunaPlayerWrapper(
                            state = state,
                            viewModel = viewModel
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            SukunaTabContentArea(
                                state = state,
                                viewModel = viewModel,
                                onOpenLogoStudio = { ch ->
                                    preselectedChannelForLogoStudio = ch
                                    viewModel.setActiveTab(SukunaTab.LOGO_STUDIO)
                                }
                            )
                        }
                    }
                }

                // Floating Status Banner Notification
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.statusBannerMessage != null,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SukunaSurfaceBright,
                        border = BorderStroke(1.dp, SukunaCyan),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.statusBannerMessage.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = viewModel::dismissBanner,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Dismiss notification",
                                    tint = SukunaCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Sukuna Live TV Opening Animation Overlay (Plays on app launch & can be replayed by tapping the "S" logo)
        androidx.compose.animation.AnimatedVisibility(
            visible = state.showOpeningSplash,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(550))
        ) {
            SukunaOpeningSplashOverlay(
                onFinished = viewModel::finishOpeningSplash
            )
        }
    }
}

@Composable
private fun NetflixMiniNowPlayingDock(
    channel: TvChannel,
    isPlaying: Boolean,
    onExpandPlayer: () -> Unit
) {
    Surface(
        onClick = onExpandPlayer,
        color = SukunaSurfaceBright.copy(alpha = 0.96f),
        border = BorderStroke(1.dp, NetflixRed.copy(alpha = 0.55f)),
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .testTag("netflix_mini_player_dock")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                ChannelAvatarIcon(
                    channel = channel,
                    isSelected = true,
                    isPlaying = isPlaying,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NetflixRed)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "LIVE TV",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = channel.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = "Tap to open Live Theater • ${channel.country} • logo/${channel.logoAssetFileName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SukunaTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedEqualizerBars(isPlaying = isPlaying)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(NetflixRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Open Live Player",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SukunaPlayerWrapper(
    state: SukunaUiState,
    viewModel: SukunaTvViewModel,
    modifier: Modifier = Modifier
) {
    val currentUrl = state.currentStream.url
    val pingResult = state.pingResults[currentUrl]
    val isFavorite = state.favoriteIds.contains(state.selectedChannel.id)

    SukunaLivePlayerSection(
        channel = state.selectedChannel,
        selectedStreamIndex = state.selectedStreamIndex,
        isPlaying = state.isPlaying,
        isBuffering = state.isBuffering,
        playerError = state.playerError,
        isFavorite = isFavorite,
        isFullscreen = state.isFullscreen,
        domainExpansionActive = state.domainExpansionActive,
        pingResult = pingResult,
        isPinging = state.isPingingCurrent,
        onPlayPauseToggle = viewModel::setPlaying,
        onBufferingChanged = viewModel::setBuffering,
        onStreamError = viewModel::tryNextServerOrChannelOnError,
        onSelectStreamServer = viewModel::selectStreamServer,
        onToggleFavorite = { viewModel.toggleFavorite(state.selectedChannel.id) },
        onToggleFullscreen = viewModel::toggleFullscreen,
        onToggleDomainAura = viewModel::toggleDomainExpansionAura,
        onPingStream = viewModel::pingStream,
        onNextChannel = {
            val list = state.filteredChannels.ifEmpty { state.allChannels }
            val currentIdx = list.indexOfFirst { it.id == state.selectedChannel.id }
            val nextChannel = if (currentIdx != -1 && currentIdx + 1 < list.size) {
                list[currentIdx + 1]
            } else {
                list.first()
            }
            viewModel.selectChannel(nextChannel)
        },
        onUrlCopied = {},
        modifier = modifier
    )
}

@Composable
private fun SukunaTabContentArea(
    state: SukunaUiState,
    viewModel: SukunaTvViewModel,
    onOpenLogoStudio: (TvChannel) -> Unit
) {
    val context = LocalContext.current
    AnimatedContent(
        targetState = state.activeTab,
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
        },
        label = "tab_content_transition"
    ) { tab ->
        when (tab) {
            SukunaTab.HOME -> {
                NetflixHomeScreen(
                    channels = state.allChannels,
                    selectedChannel = state.selectedChannel,
                    favoriteIds = state.favoriteIds,
                    homeFeedFilter = state.homeFeedFilter,
                    onFilterChanged = viewModel::setHomeFeedFilter,
                    onSelectChannel = { channel ->
                        viewModel.selectChannel(channel, navigateToPlayerTab = true)
                    },
                    onToggleFavorite = viewModel::toggleFavorite,
                    onExploreAllChannels = { countryCode ->
                        viewModel.setCountryFilter(countryCode)
                        viewModel.setActiveTab(SukunaTab.LIVE_TV)
                    },
                    onOpenLogoStudio = onOpenLogoStudio
                )
            }

            SukunaTab.LIVE_TV -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    ChannelFilterBar(
                        searchQuery = state.searchQuery,
                        selectedCountryCode = state.selectedCountryCode,
                        selectedCategory = state.selectedCategory,
                        totalCount = state.allChannels.size,
                        onSearchQueryChange = viewModel::setSearchQuery,
                        onCountrySelected = viewModel::setCountryFilter,
                        onCategorySelected = viewModel::setCategoryFilter,
                        onOpenAddStreamDialog = { viewModel.setShowAddStreamDialog(true) }
                    )

                    AnimatedChannelGrid(
                        channels = state.filteredChannels,
                        selectedChannelId = state.selectedChannel.id,
                        isPlaying = state.isPlaying,
                        favoriteIds = state.favoriteIds,
                        onSelectChannel = { channel -> viewModel.selectChannel(channel) },
                        onToggleFavorite = viewModel::toggleFavorite,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            SukunaTab.LOGO_STUDIO -> {
                ChannelLogoStudioScreen(
                    allChannels = state.allChannels,
                    logoOverridesMap = state.logoOverridesMap,
                    initialTargetChannel = null,
                    onPickLogoFileForChannel = { ch, uri ->
                        viewModel.updateChannelLogoFromFile(context, ch, uri)
                    },
                    onSaveCustomLogoPath = { ch, path ->
                        viewModel.updateChannelLogoWithCustomPath(ch, path)
                    },
                    onResetChannelLogo = viewModel::resetChannelLogo,
                    onResetAllLogos = viewModel::resetAllChannelLogos
                )
            }

            SukunaTab.API_HUB -> {
                FreeApiHubScreen(
                    providers = state.freeApis,
                    syncingApiId = state.syncingApiId,
                    syncedCounts = state.syncedApiCounts,
                    totalChannelsLoaded = state.allChannels.size,
                    onSyncProvider = viewModel::syncFreeApiProvider,
                    onSyncCustomUrl = viewModel::syncCustomApiUrl,
                    onNavigateToLiveTv = { viewModel.setActiveTab(SukunaTab.LIVE_TV) }
                )
            }

            SukunaTab.FAVORITES -> {
                FavoritesAndHistoryScreen(
                    allChannels = state.allChannels,
                    favoriteIds = state.favoriteIds,
                    recentHistory = state.recentHistory,
                    selectedChannelId = state.selectedChannel.id,
                    isPlaying = state.isPlaying,
                    onSelectChannel = { ch ->
                        viewModel.selectChannel(ch, navigateToPlayerTab = true)
                    },
                    onToggleFavorite = viewModel::toggleFavorite
                )
            }
        }
    }
}

@Composable
private fun SukunaNetflixTopBar(
    totalChannelsCount: Int,
    onReplaySplashAnimation: () -> Unit,
    onOpenLogoStudioClick: () -> Unit,
    onQuickSyncApisClick: () -> Unit,
    onAddCustomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Netflix-Red "S" Emblem + SUKUNA LIVE TV Brand Wordmark
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NetflixRed, Color(0xFF8B001D))
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .clickable(onClick = onReplaySplashAnimation)
                    .testTag("sukuna_logo_splash_trigger"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SUKUNA",
                        style = MaterialTheme.typography.titleLarge.copy(
                            letterSpacing = 1.8.sp,
                            fontWeight = FontWeight.Black,
                            color = NetflixRed
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE TV",
                        style = MaterialTheme.typography.titleMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }
                Text(
                    text = "$totalChannelsCount Live Channels • assets/logo/*.png",
                    style = MaterialTheme.typography.labelSmall,
                    color = SukunaTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Quick Intro Animation Replay + Logo Folder Studio + Free APIs + Add Stream Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = onReplaySplashAnimation,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SukunaSurfaceBright)
                    .testTag("replay_intro_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = "Replay Sukuna Intro Animation",
                    tint = SukunaCyan,
                    modifier = Modifier.size(18.dp)
                )
            }

            Surface(
                onClick = onOpenLogoStudioClick,
                shape = RoundedCornerShape(50),
                color = SukunaSurfaceBright,
                border = BorderStroke(1.dp, SukunaCyan.copy(alpha = 0.7f)),
                modifier = Modifier.testTag("header_logo_studio_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Logo Folder Studio",
                        tint = SukunaCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Logos",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            Surface(
                onClick = onQuickSyncApisClick,
                shape = RoundedCornerShape(50),
                color = SukunaSurfaceBright,
                border = BorderStroke(1.dp, NetflixRed.copy(alpha = 0.7f)),
                modifier = Modifier.testTag("header_free_apis_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Api,
                        contentDescription = "Free APIs",
                        tint = NetflixRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "APIs",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            IconButton(
                onClick = onAddCustomClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SukunaSurfaceBright)
                    .testTag("header_add_stream_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.AddLink,
                    contentDescription = "Add Custom Stream",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

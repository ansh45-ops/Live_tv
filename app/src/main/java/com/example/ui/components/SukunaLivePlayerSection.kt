package com.example.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.R
import com.example.data.StreamPingResult
import com.example.data.TvChannel
import com.example.ui.theme.SukunaBorder
import com.example.ui.theme.SukunaCrimson
import com.example.ui.theme.SukunaCyan
import com.example.ui.theme.SukunaGold
import com.example.ui.theme.SukunaObsidian
import com.example.ui.theme.SukunaSurface
import com.example.ui.theme.SukunaSurfaceBright
import com.example.ui.theme.SukunaTextSecondary
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun SukunaLivePlayerSection(
    channel: TvChannel,
    selectedStreamIndex: Int,
    isPlaying: Boolean,
    isBuffering: Boolean,
    playerError: String?,
    isFavorite: Boolean,
    isFullscreen: Boolean,
    domainExpansionActive: Boolean,
    pingResult: StreamPingResult?,
    isPinging: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    onBufferingChanged: (Boolean) -> Unit,
    onStreamError: (String) -> Unit,
    onSelectStreamServer: (Int) -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onToggleDomainAura: () -> Unit,
    onPingStream: (String) -> Unit,
    onNextChannel: () -> Unit,
    onUrlCopied: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentStream = channel.streams.getOrElse(selectedStreamIndex) { channel.streams.first() }
    var showOverlayControls by remember { mutableStateOf(true) }

    val exoPlayer = remember(context) {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Linux; Android 14) SukunaTV/2.0 ExoPlayerLib/2.19")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(12_000)
            .setReadTimeoutMs(15_000)

        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(httpDataSourceFactory)

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                playWhenReady = true
            }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> onBufferingChanged(true)
                    Player.STATE_READY -> onBufferingChanged(false)
                    Player.STATE_ENDED, Player.STATE_IDLE -> onBufferingChanged(false)
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                onPlayPauseToggle(playing)
            }

            override fun onPlayerError(error: PlaybackException) {
                onBufferingChanged(false)
                onStreamError(error.errorCodeName ?: "HLS Stream Unreachable")
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    LaunchedEffect(currentStream.url) {
        onBufferingChanged(true)
        val mediaItem = MediaItem.fromUri(currentStream.url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = isPlaying
    }

    LaunchedEffect(isPlaying) {
        if (exoPlayer.isPlaying != isPlaying) {
            if (isPlaying) exoPlayer.play() else exoPlayer.pause()
        }
    }

    // Auto-hide player overlay controls after 4 seconds when playing smoothly
    LaunchedEffect(showOverlayControls, isPlaying, playerError) {
        if (showOverlayControls && isPlaying && playerError == null) {
            delay(4500)
            showOverlayControls = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_player_section")
    ) {
        // Video Theater Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isFullscreen) Modifier.fillMaxSize()
                    else Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(20.dp))
                )
                .background(SukunaObsidian)
                .border(
                    width = if (domainExpansionActive) 2.dp else 1.dp,
                    brush = Brush.linearGradient(
                        colors = if (domainExpansionActive) {
                            listOf(SukunaCrimson, SukunaCyan, SukunaGold)
                        } else {
                            listOf(SukunaCrimson.copy(alpha = 0.6f), SukunaBorder)
                        }
                    ),
                    shape = if (isFullscreen) RoundedCornerShape(0.dp) else RoundedCornerShape(20.dp)
                )
                .clickable { showOverlayControls = !showOverlayControls }
                .testTag("video_theater_box")
        ) {
            // Subtle Sukuna studio hero artwork behind video while loading or if audio-only
            Image(
                painter = painterResource(id = R.drawable.img_sukuna_banner_1791660780921),
                contentDescription = "Sukuna TV Studio Backdrop",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.28f
            )

            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { playerView ->
                    playerView.player = exoPlayer
                },
                modifier = Modifier.fillMaxSize()
            )

            // Top HUD Bar (Always gently visible or on tap)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SukunaObsidian.copy(alpha = 0.85f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .align(Alignment.TopCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PulsingLiveBadge(isBuffering = isBuffering)
                    StreamLatencyBadge(
                        latencyMs = pingResult?.latencyMs,
                        isAlive = pingResult?.isAlive
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AnimatedEqualizerBars(isPlaying = isPlaying && !isBuffering)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${channel.flagEmoji} ${channel.countryCode.uppercase()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }

            // Center Buffering Spinner
            if (isBuffering && playerError == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = SukunaCrimson,
                        trackColor = SukunaCyan.copy(alpha = 0.25f),
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Opening HLS Manifest • ${currentStream.label}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SukunaCyan
                    )
                }
            }

            // Error Recovery Overlay (Live streams can geo-block or rotate tokens)
            if (playerError != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SukunaObsidian.copy(alpha = 0.88f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        contentDescription = "Stream Error",
                        tint = SukunaCrimson,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${channel.name} Stream Notice",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = "HLS endpoint returned ($playerError). Live streams may rotate tokens or geo-restrict.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SukunaTextSecondary,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilledTonalButton(
                            onClick = {
                                exoPlayer.prepare()
                                exoPlayer.play()
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = SukunaCrimson,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("retry_stream_button")
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retry Stream")
                        }

                        FilledTonalButton(
                            onClick = onNextChannel,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = SukunaSurfaceBright,
                                contentColor = SukunaCyan
                            ),
                            modifier = Modifier.testTag("next_channel_error_button")
                        ) {
                            Icon(Icons.Filled.SkipNext, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Next Live Channel")
                        }
                    }
                }
            }

            // Interactive Transport Overlay
            androidx.compose.animation.AnimatedVisibility(
                visible = showOverlayControls && playerError == null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    SukunaObsidian.copy(alpha = 0.92f)
                                )
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = {
                                val nextPlay = !exoPlayer.isPlaying
                                if (nextPlay) exoPlayer.play() else exoPlayer.pause()
                                onPlayPauseToggle(nextPlay)
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SukunaCrimson)
                                .testTag("play_pause_button")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = channel.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${channel.country} • ${channel.category.label} • ${currentStream.source}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SukunaTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNextChannel,
                            modifier = Modifier.testTag("skip_next_channel_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SkipNext,
                                contentDescription = "Next Channel",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.testTag("favorite_channel_button")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Toggle Favorite",
                                tint = if (isFavorite) SukunaCrimson else Color.White
                            )
                        }
                        IconButton(
                            onClick = onToggleFullscreen,
                            modifier = Modifier.testTag("fullscreen_button")
                        ) {
                            Icon(
                                imageVector = if (isFullscreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
                                contentDescription = "Toggle Fullscreen",
                                tint = SukunaCyan
                            )
                        }
                    }
                }
            }
        }

        // Multi-Server Switcher & Quick Action Strip (hidden in fullscreen)
        if (!isFullscreen) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Server switcher pills for channels with multiple HLS streams (e.g. Aaj Tak, Maasranga, RTV, ABP News)
                channel.streams.forEachIndexed { index, stream ->
                    val isCurrentServer = index == selectedStreamIndex
                    Surface(
                        onClick = { onSelectStreamServer(index) },
                        shape = RoundedCornerShape(50),
                        color = if (isCurrentServer) SukunaCrimson else SukunaSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCurrentServer) SukunaCrimson else SukunaBorder
                        ),
                        modifier = Modifier.testTag("server_pill_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Dns,
                                contentDescription = null,
                                tint = if (isCurrentServer) Color.White else SukunaCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Server #${index + 1}: ${stream.label}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = if (isCurrentServer) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Ping Stream Health Action
                Surface(
                    onClick = { onPingStream(currentStream.url) },
                    shape = RoundedCornerShape(50),
                    color = SukunaSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SukunaCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.testTag("ping_stream_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.NetworkCheck,
                            contentDescription = "Ping HLS Stream",
                            tint = SukunaCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isPinging) "Pinging…" else "Verify HLS",
                            style = MaterialTheme.typography.labelMedium,
                            color = SukunaCyan
                        )
                    }
                }

                // Domain Expansion Anime Aura Toggle
                Surface(
                    onClick = onToggleDomainAura,
                    shape = RoundedCornerShape(50),
                    color = if (domainExpansionActive) SukunaCrimson.copy(alpha = 0.28f) else SukunaSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (domainExpansionActive) SukunaGold else SukunaBorder
                    ),
                    modifier = Modifier.testTag("domain_aura_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "Sukuna Aura",
                            tint = if (domainExpansionActive) SukunaGold else SukunaTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (domainExpansionActive) "Sukuna Aura: ON" else "Sukuna Aura",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (domainExpansionActive) SukunaGold else SukunaTextSecondary
                        )
                    }
                }

                // Copy M3U8 URL Action
                Surface(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(currentStream.url))
                        onUrlCopied()
                    },
                    shape = RoundedCornerShape(50),
                    color = SukunaSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SukunaBorder),
                    modifier = Modifier.testTag("copy_m3u8_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Copy M3U8 URL",
                            tint = SukunaTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Copy .m3u8",
                            style = MaterialTheme.typography.labelMedium,
                            color = SukunaTextSecondary
                        )
                    }
                }
            }
        }
    }
}

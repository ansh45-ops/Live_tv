package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChannelCategory
import com.example.data.FreeApiProvider
import com.example.data.StreamPingResult
import com.example.data.TvChannel
import com.example.data.WatchHistoryEntity
import com.example.ui.theme.SukunaBorder
import com.example.ui.theme.SukunaCrimson
import com.example.ui.theme.SukunaCyan
import com.example.ui.theme.SukunaEmerald
import com.example.ui.theme.SukunaGold
import com.example.ui.theme.SukunaObsidian
import com.example.ui.theme.SukunaSurface
import com.example.ui.theme.SukunaSurfaceBright
import com.example.ui.theme.SukunaTextMuted
import com.example.ui.theme.SukunaTextSecondary

@Composable
fun FreeApiHubScreen(
    providers: List<FreeApiProvider>,
    syncingApiId: String?,
    syncedCounts: Map<String, Int>,
    totalChannelsLoaded: Int,
    onSyncProvider: (FreeApiProvider) -> Unit,
    onSyncCustomUrl: (String, String) -> Unit,
    onNavigateToLiveTv: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customApiUrl by remember { mutableStateOf("") }
    var customApiLabel by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("free_api_hub_list"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header Summary Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurfaceBright),
                border = BorderStroke(1.5.dp, SukunaCrimson.copy(alpha = 0.65f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    SukunaCrimson.copy(alpha = 0.24f),
                                    SukunaCyan.copy(alpha = 0.12f),
                                    SukunaSurfaceBright
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SukunaCrimson),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Api,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Sukuna Multi-API Stream Engine",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "FFxAPIs/IcX Core + 8 Open Global IPTV APIs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SukunaCyan
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(SukunaEmerald.copy(alpha = 0.18f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "$totalChannelsLoaded Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = SukunaEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tap 'Sync Live API' on any free directory below to fetch & parse real-time .m3u8 / JSON channels directly into your Sukuna TV player.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SukunaTextSecondary
                    )
                }
            }
        }

        // Custom Free API URL Sync Box
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                border = BorderStroke(1.dp, SukunaBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Link,
                            contentDescription = null,
                            tint = SukunaGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Connect Any External M3U8 or JSON API URL",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customApiLabel,
                        onValueChange = { customApiLabel = it },
                        placeholder = { Text("API Provider Label (e.g., Sports M3U API)", color = SukunaTextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCyan,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_api_label_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customApiUrl,
                        onValueChange = { customApiUrl = it },
                        placeholder = { Text("https://iptv-org.github.io/iptv/index.m3u", color = SukunaTextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCrimson,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_api_url_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (customApiUrl.isNotBlank()) {
                                onSyncCustomUrl(customApiUrl, customApiLabel)
                                customApiUrl = ""
                            }
                        },
                        enabled = customApiUrl.isNotBlank() && syncingApiId == null,
                        colors = ButtonDefaults.buttonColors(containerColor = SukunaCrimson),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sync_custom_api_button")
                    ) {
                        Icon(Icons.Filled.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Fetch & Merge Live Channels")
                    }
                }
            }
        }

        // Pre-configured Free IPTV APIs List
        items(providers, key = { it.id }) { provider ->
            val isSyncing = syncingApiId == provider.id
            val syncedCount = syncedCounts[provider.id]

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (syncedCount != null) SukunaEmerald.copy(alpha = 0.5f) else SukunaBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("api_provider_card_${provider.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = provider.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = provider.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = SukunaTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SukunaCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = provider.regionTag,
                                style = MaterialTheme.typography.labelSmall,
                                color = SukunaCyan
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Endpoint URL preview
                    Text(
                        text = provider.endpointUrl,
                        style = MaterialTheme.typography.labelSmall,
                        color = SukunaTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SukunaSurfaceBright)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = provider.format,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SukunaGold
                                )
                            }

                            Text(
                                text = if (syncedCount != null) "$syncedCount Channels Ready" else provider.estimatedChannels,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (syncedCount != null) SukunaEmerald else SukunaTextSecondary
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (syncedCount != null) {
                                OutlinedButton(
                                    onClick = onNavigateToLiveTv,
                                    border = BorderStroke(1.dp, SukunaEmerald),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = null,
                                        tint = SukunaEmerald,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Watch", color = SukunaEmerald, style = MaterialTheme.typography.labelMedium)
                                }
                            }

                            FilledTonalButton(
                                onClick = { onSyncProvider(provider) },
                                enabled = syncingApiId == null,
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = SukunaCrimson,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("sync_api_btn_${provider.id}")
                            ) {
                                if (isSyncing) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Syncing…", style = MaterialTheme.typography.labelMedium)
                                } else {
                                    Icon(
                                        imageVector = if (syncedCount != null) Icons.Filled.CheckCircle else Icons.Filled.CloudDownload,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = if (provider.isPreloaded) "Refresh API" else "Sync Live API",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesAndHistoryScreen(
    allChannels: List<TvChannel>,
    favoriteIds: Set<Int>,
    recentHistory: List<WatchHistoryEntity>,
    selectedChannelId: Int,
    isPlaying: Boolean,
    onSelectChannel: (TvChannel) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteChannels = remember(allChannels, favoriteIds) {
        allChannels.filter { favoriteIds.contains(it.id) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_history_screen"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = SukunaCrimson,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Saved Favorite Channels (${favoriteChannels.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }

        if (favoriteChannels.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                    border = BorderStroke(1.dp, SukunaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = SukunaCrimson.copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Favorite Channels Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            text = "Tap the heart icon on any Bangladesh, India, or Global channel card to pin it here for instant access.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SukunaTextSecondary
                        )
                    }
                }
            }
        } else {
            items(favoriteChannels, key = { "fav_${it.id}" }) { channel ->
                val isSelected = channel.id == selectedChannelId
                AnimatedChannelCard(
                    channel = channel,
                    isSelected = isSelected,
                    isPlaying = isSelected && isPlaying,
                    isFavorite = true,
                    onClick = { onSelectChannel(channel) },
                    onFavoriteClick = { onToggleFavorite(channel.id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = SukunaCyan,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recently Watched Streams",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }

        if (recentHistory.isEmpty()) {
            item {
                Text(
                    text = "Select any live channel to start building your playback history.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SukunaTextSecondary
                )
            }
        } else {
            items(recentHistory, key = { "hist_${it.channelId}" }) { item ->
                val matchedChannel = allChannels.find { it.id == item.channelId }
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                    border = BorderStroke(1.dp, SukunaBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (matchedChannel != null) onSelectChannel(matchedChannel)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (matchedChannel != null) {
                                ChannelAvatarIcon(
                                    channel = matchedChannel,
                                    isSelected = matchedChannel.id == selectedChannelId,
                                    isPlaying = isPlaying,
                                    size = 42.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.channelName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                                Text(
                                    text = item.streamUrl,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SukunaTextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play",
                            tint = SukunaCrimson
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StreamLabScreen(
    currentChannel: TvChannel,
    pingResults: Map<String, StreamPingResult>,
    isPinging: Boolean,
    onPingUrl: (String) -> Unit,
    onImportRawPayload: (String, String) -> Unit,
    onOpenAddSingleDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var probeUrl by remember(currentChannel) {
        mutableStateOf(currentChannel.streams.first().url)
    }
    var rawJsonOrM3u by remember { mutableStateOf("") }
    var importLabel by remember { mutableStateOf("FFxAPIs/IcX Custom Batch") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("stream_lab_screen"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Live HLS Stream Inspector Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                border = BorderStroke(1.dp, SukunaCyan.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Radar,
                            contentDescription = null,
                            tint = SukunaCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "HLS Manifest & HTTP 200/206 Verifier",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Inspect any .m3u8 stream for HTTP status, latency, and MIME manifest headers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SukunaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = probeUrl,
                        onValueChange = { probeUrl = it },
                        label = { Text("HLS Stream URL (.m3u8)", color = SukunaCyan) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCyan,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("probe_url_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onPingUrl(probeUrl) },
                            enabled = probeUrl.isNotBlank() && !isPinging,
                            colors = ButtonDefaults.buttonColors(containerColor = SukunaCyan, contentColor = SukunaObsidian),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("run_ping_test_button")
                        ) {
                            Icon(Icons.Filled.NetworkCheck, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPinging) "Probing Manifest…" else "Verify Stream Health",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onOpenAddSingleDialog,
                            border = BorderStroke(1.dp, SukunaCrimson),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.AddCircle, contentDescription = null, tint = SukunaCrimson, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Channel", color = Color.White)
                        }
                    }

                    val latestResult = pingResults[probeUrl]
                    if (latestResult != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SukunaObsidian)
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (latestResult.isAlive) "STATUS: VERIFIED ONLINE" else "STATUS: UNREACHABLE / GEO",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (latestResult.isAlive) SukunaEmerald else SukunaCrimson
                                    )
                                    Text(
                                        text = "HTTP ${latestResult.statusCode} • ${latestResult.latencyMs}ms",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SukunaGold
                                    )
                                }
                                Text(
                                    text = "Content-Type: ${latestResult.contentType}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SukunaTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // JSON / M3U Batch Payload Importer (Supports FFxAPIs/IcX JSON schema directly)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                border = BorderStroke(1.dp, SukunaBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Code,
                            contentDescription = null,
                            tint = SukunaCrimson,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FFxAPIs/IcX JSON & M3U8 Batch Importer",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Paste raw JSON (matching schema_version 1.0 channels array) or raw #EXTM3U playlists to import channels in bulk.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SukunaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = importLabel,
                        onValueChange = { importLabel = it },
                        label = { Text("Source Tag", color = SukunaTextSecondary) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCrimson,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rawJsonOrM3u,
                        onValueChange = { rawJsonOrM3u = it },
                        placeholder = {
                            Text(
                                text = "#EXTM3U\n#EXTINF:-1 tvg-country=\"bd\",My Channel\nhttps://example.com/live/index.m3u8",
                                color = SukunaTextMuted
                            )
                        },
                        minLines = 4,
                        maxLines = 8,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCrimson,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("raw_payload_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            onImportRawPayload(rawJsonOrM3u, importLabel)
                            rawJsonOrM3u = ""
                        },
                        enabled = rawJsonOrM3u.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = SukunaCrimson),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("import_payload_button")
                    ) {
                        Icon(Icons.Filled.ContentPaste, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Parse & Add to Sukuna TV")
                    }
                }
            }
        }
    }
}

@Composable
fun AddCustomStreamDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, country: String, countryCode: String, category: ChannelCategory, url: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("Bangladesh") }
    var countryCode by remember { mutableStateOf("bd") }
    var streamUrl by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ChannelCategory.ENTERTAINMENT) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SukunaSurfaceBright,
        titleContentColor = Color.White,
        textContentColor = SukunaTextSecondary,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AddCircle, contentDescription = null, tint = SukunaCrimson)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Live HLS Channel", style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Channel Name") },
                    placeholder = { Text("e.g., Sukuna Anime HD") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SukunaCrimson,
                        unfocusedBorderColor = SukunaBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_channel_name_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCyan,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = countryCode,
                        onValueChange = { countryCode = it.take(2) },
                        label = { Text("ISO") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCyan,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.width(82.dp)
                    )
                }

                OutlinedTextField(
                    value = streamUrl,
                    onValueChange = { streamUrl = it },
                    label = { Text("HLS Stream URL (.m3u8)") },
                    placeholder = { Text("https://.../index.m3u8") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SukunaCrimson,
                        unfocusedBorderColor = SukunaBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_stream_url_input")
                )

                Text(
                    text = "Select Category Icon:",
                    style = MaterialTheme.typography.labelMedium,
                    color = SukunaCyan
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ChannelCategory.entries.filter { it != ChannelCategory.ALL }.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            leadingIcon = {
                                Icon(
                                    imageVector = categoryIcon(cat),
                                    contentDescription = null,
                                    tint = if (isSelected) SukunaObsidian else SukunaCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            label = { Text(cat.badgeText) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SukunaCyan,
                                selectedLabelColor = SukunaObsidian,
                                containerColor = SukunaSurface,
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && streamUrl.isNotBlank()) {
                        onConfirm(name, country, countryCode, selectedCategory, streamUrl)
                    }
                },
                enabled = name.isNotBlank() && streamUrl.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SukunaCrimson),
                modifier = Modifier.testTag("dialog_confirm_add_button")
            ) {
                Text("Save Channel")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SukunaTextSecondary)
            }
        }
    )
}

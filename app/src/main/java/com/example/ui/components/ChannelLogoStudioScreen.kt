package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TvChannel
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

private val NetflixRed = Color(0xFFE50914)

@Composable
fun ChannelLogoStudioScreen(
    allChannels: List<TvChannel>,
    logoOverridesMap: Map<Int, String>,
    initialTargetChannel: TvChannel?,
    onPickLogoFileForChannel: (TvChannel, Uri) -> Unit,
    onSaveCustomLogoPath: (TvChannel, String) -> Unit,
    onResetChannelLogo: (TvChannel) -> Unit,
    onResetAllLogos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var activeChannelForPicker by remember { mutableStateOf<TvChannel?>(initialTargetChannel) }
    var channelForUrlDialog by remember { mutableStateOf<TvChannel?>(null) }

    // Zero-permission Android Photo Picker for selecting a PNG/JPG/WebP logo file from device
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val target = activeChannelForPicker
        if (uri != null && target != null) {
            onPickLogoFileForChannel(target, uri)
        }
    }

    val filteredChannels = remember(allChannels, searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) allChannels
        else allChannels.filter {
            it.name.lowercase().contains(q) ||
                it.logoAssetFileName.lowercase().contains(q) ||
                it.country.lowercase().contains(q)
        }
    }

    if (channelForUrlDialog != null) {
        val target = channelForUrlDialog!!
        var pathInput by remember(target) {
            mutableStateOf(logoOverridesMap[target.id] ?: target.defaultAssetLogoUri)
        }
        AlertDialog(
            onDismissRequest = { channelForUrlDialog = null },
            containerColor = SukunaSurfaceBright,
            titleContentColor = Color.White,
            textContentColor = SukunaTextSecondary,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = SukunaCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Logo File: ${target.name}", style = MaterialTheme.typography.titleLarge)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ChannelAvatarIcon(
                            channel = target,
                            isSelected = true,
                            isPlaying = false,
                            size = 60.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Default Folder File:",
                                style = MaterialTheme.typography.labelSmall,
                                color = SukunaCyan
                            )
                            Text(
                                text = "app/src/main/assets/logo/${target.logoAssetFileName}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "You can either pick an image file from your device storage, replace `app/src/main/assets/logo/${target.logoAssetFileName}` in the project file tree, or enter a custom file/URL path below:",
                        style = MaterialTheme.typography.bodySmall,
                        color = SukunaTextSecondary
                    )

                    OutlinedTextField(
                        value = pathInput,
                        onValueChange = { pathInput = it },
                        label = { Text("Logo File Path or Image URL") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SukunaCyan,
                            unfocusedBorderColor = SukunaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            activeChannelForPicker = target
                            channelForUrlDialog = null
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SukunaCyan,
                            contentColor = SukunaObsidian
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Choose Image File from Device", fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveCustomLogoPath(target, pathInput)
                        channelForUrlDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Save Logo Path")
                }
            },
            dismissButton = {
                TextButton(onClick = { channelForUrlDialog = null }) {
                    Text("Cancel", color = SukunaTextSecondary)
                }
            }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 165.dp),
        contentPadding = PaddingValues(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("logo_studio_grid")
    ) {
        // Header Explaining `app/src/main/assets/logo/` Folder & File Picker
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurfaceBright),
                border = BorderStroke(1.5.dp, SukunaCyan.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    NetflixRed.copy(alpha = 0.22f),
                                    SukunaCyan.copy(alpha = 0.14f),
                                    SukunaSurfaceBright
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NetflixRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FolderOpen,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Channel Logo Folder Studio",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "Folder: app/src/main/assets/logo/*.png",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SukunaCyan
                                )
                            }
                        }

                        if (logoOverridesMap.isNotEmpty()) {
                            OutlinedButton(
                                onClick = onResetAllLogos,
                                border = BorderStroke(1.dp, SukunaGold),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Restore,
                                    contentDescription = null,
                                    tint = SukunaGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Reset (${logoOverridesMap.size})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SukunaGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Every channel has its own PNG logo file inside `app/src/main/assets/logo/` (e.g., `ananda_tv.png`, `t_sports.png`, `aaj_tak.png`). Replace any file in that folder in the code editor OR tap 'Change File' on any card below to pick a new logo from your device anytime!",
                        style = MaterialTheme.typography.bodySmall,
                        color = SukunaTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Search channel name or logo filename (e.g., t_sports.png)…",
                                style = MaterialTheme.typography.bodySmall,
                                color = SukunaTextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = SukunaCyan)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = SukunaTextSecondary)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NetflixRed,
                            unfocusedBorderColor = SukunaBorder,
                            focusedContainerColor = SukunaObsidian.copy(alpha = 0.6f),
                            unfocusedContainerColor = SukunaObsidian.copy(alpha = 0.6f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("logo_studio_search_input")
                    )
                }
            }
        }

        // Individual Channel Logo Cards
        items(filteredChannels, key = { "logo_item_${it.id}" }) { channel ->
            val hasCustomOverride = logoOverridesMap.containsKey(channel.id)

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SukunaSurface),
                border = BorderStroke(
                    width = if (hasCustomOverride) 1.8.dp else 1.dp,
                    color = if (hasCustomOverride) SukunaEmerald else SukunaBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("logo_card_${channel.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top status pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (hasCustomOverride) SukunaEmerald.copy(alpha = 0.18f)
                                    else SukunaCyan.copy(alpha = 0.14f)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (hasCustomOverride) "CUSTOM FILE" else "ASSETS/LOGO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (hasCustomOverride) SukunaEmerald else SukunaCyan,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        if (hasCustomOverride) {
                            IconButton(
                                onClick = { onResetChannelLogo(channel) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Restore,
                                    contentDescription = "Reset logo",
                                    tint = SukunaGold,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Channel Logo Preview
                    ChannelAvatarIcon(
                        channel = channel,
                        isSelected = hasCustomOverride,
                        isPlaying = false,
                        size = 72.dp,
                        modifier = Modifier.clickable {
                            activeChannelForPicker = channel
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = channel.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "logo/${channel.logoAssetFileName}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SukunaTextMuted,
                            fontSize = 9.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Buttons: Pick File from Device or Edit Path
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                activeChannelForPicker = channel
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = NetflixRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(34.dp)
                                .testTag("pick_logo_file_btn_${channel.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Change File",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }

                        Surface(
                            onClick = { channelForUrlDialog = channel },
                            shape = RoundedCornerShape(10.dp),
                            color = SukunaSurfaceBright,
                            border = BorderStroke(1.dp, SukunaBorder),
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("edit_logo_path_btn_${channel.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Link,
                                    contentDescription = "Edit Logo Path",
                                    tint = SukunaCyan,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

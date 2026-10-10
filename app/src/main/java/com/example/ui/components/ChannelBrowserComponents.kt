package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChannelCategory
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

data class CountryFilterOption(
    val code: String,
    val label: String,
    val flag: String
)

val countryFilterOptions = listOf(
    CountryFilterOption("ALL", "All Regions", "🌐"),
    CountryFilterOption("bd", "Bangladesh", "🇧🇩"),
    CountryFilterOption("in", "India", "🇮🇳"),
    CountryFilterOption("jp", "Japan", "🇯🇵"),
    CountryFilterOption("kr", "Korea", "🇰🇷"),
    CountryFilterOption("us", "USA", "🇺🇸"),
    CountryFilterOption("gb", "UK", "🇬🇧"),
    CountryFilterOption("qa", "Global News", "🇶🇦")
)

@Composable
fun ChannelFilterBar(
    searchQuery: String,
    selectedCountryCode: String,
    selectedCategory: ChannelCategory,
    totalCount: Int,
    onSearchQueryChange: (String) -> Unit,
    onCountrySelected: (String) -> Unit,
    onCategorySelected: (ChannelCategory) -> Unit,
    onOpenAddStreamDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        // Search Input + Quick Add Custom Stream Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = "Search $totalCount+ live channels, BD, IN, Anime…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SukunaTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = SukunaCyan
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.testTag("clear_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear search",
                                tint = SukunaTextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SukunaCrimson,
                    unfocusedBorderColor = SukunaBorder,
                    focusedContainerColor = SukunaSurface,
                    unfocusedContainerColor = SukunaSurface.copy(alpha = 0.85f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("channel_search_input")
            )

            Surface(
                onClick = onOpenAddStreamDialog,
                shape = RoundedCornerShape(16.dp),
                color = SukunaCrimson,
                modifier = Modifier
                    .height(54.dp)
                    .testTag("open_add_stream_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddLink,
                        contentDescription = "Add Stream",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add M3U",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Country Filter Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            countryFilterOptions.forEach { country ->
                val isSelected = selectedCountryCode.equals(country.code, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onCountrySelected(country.code) },
                    label = {
                        Text(
                            text = "${country.flag} ${country.label}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SukunaCrimson,
                        selectedLabelColor = Color.White,
                        containerColor = SukunaSurface,
                        labelColor = SukunaTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = SukunaBorder,
                        selectedBorderColor = SukunaCrimson
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("country_chip_${country.code.lowercase()}")
                )
            }
        }

        // Category Filter Ribbon with All Icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChannelCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    leadingIcon = {
                        Icon(
                            imageVector = categoryIcon(category),
                            contentDescription = null,
                            tint = if (isSelected) SukunaObsidian else SukunaCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        Text(
                            text = category.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SukunaCyan,
                        selectedLabelColor = SukunaObsidian,
                        containerColor = SukunaSurface,
                        labelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = SukunaBorder,
                        selectedBorderColor = SukunaCyan
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
                )
            }
        }
    }
}

@Composable
fun AnimatedChannelGrid(
    channels: List<TvChannel>,
    selectedChannelId: Int,
    isPlaying: Boolean,
    favoriteIds: Set<Int>,
    onSelectChannel: (TvChannel) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (channels.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = SukunaCrimson,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No Matching Live Channels",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Try clearing your search or sync more Free IPTV APIs from the Free APIs tab.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SukunaTextSecondary
                )
            }
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 165.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("channels_lazy_grid")
    ) {
        items(
            items = channels,
            key = { it.id }
        ) { channel ->
            val isSelected = channel.id == selectedChannelId
            val isFav = favoriteIds.contains(channel.id)
            AnimatedChannelCard(
                channel = channel,
                isSelected = isSelected,
                isPlaying = isSelected && isPlaying,
                isFavorite = isFav,
                onClick = { onSelectChannel(channel) },
                onFavoriteClick = { onToggleFavorite(channel.id) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun AnimatedChannelCard(
    channel: TvChannel,
    isSelected: Boolean,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 420f),
        label = "card_scale"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) SukunaCrimson else SukunaBorder,
        label = "border_color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clickable(onClick = onClick)
            .testTag("channel_card_${channel.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SukunaSurfaceBright else SukunaSurface
        ),
        border = BorderStroke(
            width = if (isSelected) 1.8.dp else 1.dp,
            color = borderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = if (isSelected) {
                            listOf(
                                SukunaCrimson.copy(alpha = 0.18f),
                                SukunaSurfaceBright
                            )
                        } else {
                            listOf(
                                SukunaSurface,
                                SukunaObsidian.copy(alpha = 0.65f)
                            )
                        }
                    )
                )
                .padding(12.dp)
        ) {
            // Top Row: Avatar Icon + Equalizer / Favorite button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ChannelAvatarIcon(
                    channel = channel,
                    isSelected = isSelected,
                    isPlaying = isPlaying,
                    size = 50.dp
                )

                Column(horizontalAlignment = Alignment.End) {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("fav_btn_${channel.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite ${channel.name}",
                            tint = if (isFavorite) SukunaCrimson else SukunaTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    if (isSelected) {
                        AnimatedEqualizerBars(
                            isPlaying = isPlaying,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Channel Title
            Text(
                text = channel.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Country Flag + Category Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${channel.flagEmoji} ${channel.country}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SukunaTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SukunaCyan.copy(alpha = 0.14f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = channel.category.badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SukunaCyan,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer Metadata: Verified + Multi-server count + Source
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Verified HLS",
                        tint = SukunaEmerald,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = channel.streams.first().source,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SukunaTextMuted,
                            fontSize = 9.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (channel.streams.size > 1) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SukunaGold.copy(alpha = 0.18f))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Dns,
                            contentDescription = null,
                            tint = SukunaGold,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${channel.streams.size}x",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SukunaGold,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

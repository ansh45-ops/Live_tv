package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ChannelCategory
import com.example.data.TvChannel
import com.example.ui.HomeFeedFilter
import com.example.ui.theme.SpaceGroteskFamily
import com.example.ui.theme.SukunaBorder
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
fun NetflixHomeScreen(
    channels: List<TvChannel>,
    selectedChannel: TvChannel,
    favoriteIds: Set<Int>,
    homeFeedFilter: HomeFeedFilter,
    onFilterChanged: (HomeFeedFilter) -> Unit,
    onSelectChannel: (TvChannel) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onExploreAllChannels: (String) -> Unit,
    onOpenLogoStudio: (TvChannel) -> Unit,
    modifier: Modifier = Modifier
) {
    val spotlightCandidates = remember(channels) {
        // Feature top flagship channels with multi-server or high demand (T Sports, Aaj Tak, Somoy TV, Maasranga, Red Bull TV, Goldmines)
        channels.filter {
            it.name in listOf("T Sports", "Aaj Tak", "Somoy TV", "Maasranga TV", "RTV", "ABP News", "Red Bull TV Global", "RetroCrush Anime TV")
        }.ifEmpty { channels.take(6) }
    }
    var spotlightIndex by remember { mutableIntStateOf(0) }
    val featuredChannel = spotlightCandidates.getOrElse(spotlightIndex % spotlightCandidates.size.coerceAtLeast(1)) {
        selectedChannel
    }

    val bdChannels = remember(channels) {
        channels.filter { it.countryCode.equals("bd", ignoreCase = true) }
    }
    val inChannels = remember(channels) {
        channels.filter { it.countryCode.equals("in", ignoreCase = true) }
    }
    val sportsAndNewsChannels = remember(channels) {
        channels.filter { it.category == ChannelCategory.SPORTS || it.category == ChannelCategory.NEWS }
    }
    val animeAndMusicChannels = remember(channels) {
        channels.filter { it.category == ChannelCategory.KIDS || it.category == ChannelCategory.MUSIC }
    }
    val cinemaAndDramaChannels = remember(channels) {
        channels.filter { it.category == ChannelCategory.MOVIES || it.category == ChannelCategory.ENTERTAINMENT }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("netflix_home_lazy_column"),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // 1. NETFLIX LIVE TV HERO BILLBOARD (Live TV Only)
        item {
            NetflixLiveHeroBillboard(
                featuredChannel = featuredChannel,
                isFavorite = favoriteIds.contains(featuredChannel.id),
                activeFilter = homeFeedFilter,
                onFilterSelected = onFilterChanged,
                onPlayLiveClick = { onSelectChannel(featuredChannel) },
                onNextSpotlightClick = { spotlightIndex++ },
                onToggleFavoriteClick = { onToggleFavorite(featuredChannel.id) },
                onChangeLogoClick = { onOpenLogoStudio(featuredChannel) }
            )
        }

        // 2. SECTION 1: NETFLIX TOP 10 LIVE TV CHANNELS TODAY
        if (homeFeedFilter == HomeFeedFilter.ALL ||
            homeFeedFilter == HomeFeedFilter.BANGLADESH ||
            homeFeedFilter == HomeFeedFilter.INDIA
        ) {
            item {
                NetflixSectionHeader(
                    badge = "TOP 10 LIVE TV",
                    title = "Top 10 Live TV Channels Today",
                    subtitle = "Most watched verified HLS streams on Sukuna Live TV",
                    badgeColor = NetflixRed,
                    actionLabel = "All ${channels.size} Live",
                    onActionClick = { onExploreAllChannels("ALL") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                val top10List = remember(channels, homeFeedFilter) {
                    when (homeFeedFilter) {
                        HomeFeedFilter.BANGLADESH -> bdChannels.take(10)
                        HomeFeedFilter.INDIA -> inChannels.take(10)
                        else -> channels.take(10)
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.testTag("top_10_live_row")
                ) {
                    itemsIndexed(top10List, key = { _, ch -> "top10_${ch.id}" }) { index, channel ->
                        NetflixTop10ChannelCard(
                            rank = index + 1,
                            channel = channel,
                            isSelected = channel.id == selectedChannel.id,
                            onClick = { onSelectChannel(channel) }
                        )
                    }
                }
            }
        }

        // 3. SECTION 2: BANGLADESH LIVE TV ORIGINALS (🇧🇩 FFxAPIs/IcX + AynaOTT)
        if (homeFeedFilter == HomeFeedFilter.ALL || homeFeedFilter == HomeFeedFilter.BANGLADESH) {
            item {
                NetflixSectionHeader(
                    badge = "🇧🇩 BANGLADESH LIVE",
                    title = "Bangladesh Live TV Networks",
                    subtitle = "${bdChannels.size} Channels • Logo Folder: assets/logo/*.png",
                    badgeColor = SukunaEmerald,
                    actionLabel = "Browse BD Grid",
                    onActionClick = { onExploreAllChannels("bd") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.testTag("bd_live_row")
                ) {
                    items(bdChannels, key = { "bd_${it.id}" }) { channel ->
                        NetflixLiveChannelWideCard(
                            channel = channel,
                            isSelected = channel.id == selectedChannel.id,
                            isFavorite = favoriteIds.contains(channel.id),
                            onClick = { onSelectChannel(channel) },
                            onEditLogoClick = { onOpenLogoStudio(channel) }
                        )
                    }
                }
            }
        }

        // 4. SECTION 3: INDIA PRIME LIVE TV NETWORKS (🇮🇳 Aaj Tak, ABP, DD Sports, Epic, 9XM)
        if (homeFeedFilter == HomeFeedFilter.ALL || homeFeedFilter == HomeFeedFilter.INDIA) {
            item {
                NetflixSectionHeader(
                    badge = "🇮🇳 INDIA LIVE",
                    title = "India Prime Live TV & Regional",
                    subtitle = "${inChannels.size} Channels • Aaj Tak, ABP, Epic Bharat, DD Sports",
                    badgeColor = NetflixRed,
                    actionLabel = "Browse India Grid",
                    onActionClick = { onExploreAllChannels("in") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.testTag("in_live_row")
                ) {
                    items(inChannels, key = { "in_${it.id}" }) { channel ->
                        NetflixLiveChannelWideCard(
                            channel = channel,
                            isSelected = channel.id == selectedChannel.id,
                            isFavorite = favoriteIds.contains(channel.id),
                            onClick = { onSelectChannel(channel) },
                            onEditLogoClick = { onOpenLogoStudio(channel) }
                        )
                    }
                }
            }
        }

        // 5. SECTION 4: LIVE SPORTS ARENA & BREAKING NEWS 24/7
        if (homeFeedFilter == HomeFeedFilter.ALL || homeFeedFilter == HomeFeedFilter.SPORTS_NEWS) {
            item {
                NetflixSectionHeader(
                    badge = "SPORTS & NEWS 24/7",
                    title = "Live Sports Arena & Global News",
                    subtitle = "T Sports, Gazi TV, DD Sports, Red Bull TV, Al Jazeera, Sky News",
                    badgeColor = SukunaGold
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sportsAndNewsChannels, key = { "spt_${it.id}" }) { channel ->
                        NetflixLiveChannelWideCard(
                            channel = channel,
                            isSelected = channel.id == selectedChannel.id,
                            isFavorite = favoriteIds.contains(channel.id),
                            onClick = { onSelectChannel(channel) },
                            onEditLogoClick = { onOpenLogoStudio(channel) }
                        )
                    }
                }
            }
        }

        // 6. SECTION 5: ANIME, KIDS & NON-STOP MUSIC TV
        if (homeFeedFilter == HomeFeedFilter.ALL || homeFeedFilter == HomeFeedFilter.ANIME_MUSIC) {
            item {
                NetflixSectionHeader(
                    badge = "ANIME & MUSIC TV",
                    title = "Live Anime, Kids & Music Channels",
                    subtitle = "RetroCrush Anime, Duronto TV, Epic Kids, 9XM, Dhoom Music",
                    badgeColor = Color(0xFFFF4D94)
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(animeAndMusicChannels, key = { "anime_${it.id}" }) { channel ->
                        NetflixLiveChannelWideCard(
                            channel = channel,
                            isSelected = channel.id == selectedChannel.id,
                            isFavorite = favoriteIds.contains(channel.id),
                            onClick = { onSelectChannel(channel) },
                            onEditLogoClick = { onOpenLogoStudio(channel) }
                        )
                    }
                }
            }
        }

        // 7. SECTION 6: GENERAL ENTERTAINMENT & 24/7 CINEMA TV CHANNELS
        if (homeFeedFilter == HomeFeedFilter.ALL) {
            item {
                NetflixSectionHeader(
                    badge = "ENTERTAINMENT & TV",
                    title = "General Entertainment & Drama TV",
                    subtitle = "Ananda TV, Maasranga, RTV, Boishakhi, Epic Bharat, Goldmines TV",
                    badgeColor = SukunaCyan
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cinemaAndDramaChannels, key = { "ent_${it.id}" }) { channel ->
                        NetflixLiveChannelWideCard(
                            channel = channel,
                            isSelected = channel.id == selectedChannel.id,
                            isFavorite = favoriteIds.contains(channel.id),
                            onClick = { onSelectChannel(channel) },
                            onEditLogoClick = { onOpenLogoStudio(channel) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NetflixLiveHeroBillboard(
    featuredChannel: TvChannel,
    isFavorite: Boolean,
    activeFilter: HomeFeedFilter,
    onFilterSelected: (HomeFeedFilter) -> Unit,
    onPlayLiveClick: () -> Unit,
    onNextSpotlightClick: () -> Unit,
    onToggleFavoriteClick: () -> Unit,
    onChangeLogoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .height(420.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NetflixRed.copy(alpha = 0.75f),
                        SukunaCyan.copy(alpha = 0.4f),
                        SukunaBorder
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .testTag("netflix_hero_billboard")
    ) {
        // Generated Sukuna Broadcast Backdrop
        Image(
            painter = painterResource(id = R.drawable.img_sukuna_banner_1791660780921),
            contentDescription = featuredChannel.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Deep Netflix bottom & top vignette gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to SukunaObsidian.copy(alpha = 0.85f),
                        0.28f to SukunaObsidian.copy(alpha = 0.25f),
                        0.62f to SukunaObsidian.copy(alpha = 0.78f),
                        1.0f to SukunaObsidian.copy(alpha = 0.98f)
                    )
                )
        )

        // Top Netflix Category Filter Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeFeedFilter.entries.forEach { filter ->
                val isSelected = activeFilter == filter
                Surface(
                    onClick = { onFilterSelected(filter) },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) NetflixRed else SukunaObsidian.copy(alpha = 0.65f),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) NetflixRed else Color.White.copy(alpha = 0.35f)
                    ),
                    modifier = Modifier.testTag("netflix_filter_${filter.name.lowercase()}")
                ) {
                    Text(
                        text = filter.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // Center-Bottom Featured Live Channel Showcase (with its Logo from assets/logo/<slug>.png)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Featured Channel Logo Badge from assets/logo/
            ChannelAvatarIcon(
                channel = featuredChannel,
                isSelected = true,
                isPlaying = true,
                size = 84.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // "S" LIVE BROADCAST BADGE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NetflixRed)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "S",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LIVE TELEVISION • ${featuredChannel.streams.first().source}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.8.sp,
                        color = Color.White.copy(alpha = 0.92f),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = featuredChannel.name,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Country + Category + Verified HLS + Logo File Path
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${featuredChannel.flagEmoji} ${featuredChannel.country}",
                    style = MaterialTheme.typography.labelLarge,
                    color = SukunaEmerald
                )
                Text(text = "•", color = SukunaTextSecondary)
                Text(
                    text = featuredChannel.category.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
                Box(
                    modifier = Modifier
                        .border(1.dp, SukunaCyan, RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "logo/${featuredChannel.logoAssetFileName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SukunaCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Netflix Signature Action Row: White Watch Live Button + My List + Change Logo + Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPlayLiveClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("hero_play_live_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Watch Live",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Watch Live",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    )
                }

                OutlinedButton(
                    onClick = onToggleFavoriteClick,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SukunaSurfaceBright.copy(alpha = 0.75f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(46.dp)
                        .testTag("hero_my_list_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Check else Icons.Filled.Add,
                        contentDescription = "My List",
                        tint = if (isFavorite) SukunaEmerald else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("My List")
                }

                Surface(
                    onClick = onChangeLogoClick,
                    shape = RoundedCornerShape(10.dp),
                    color = SukunaSurfaceBright.copy(alpha = 0.75f),
                    border = BorderStroke(1.dp, SukunaCyan.copy(alpha = 0.55f)),
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("hero_change_logo_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "Change Logo File",
                            tint = SukunaCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    onClick = onNextSpotlightClick,
                    shape = RoundedCornerShape(10.dp),
                    color = SukunaSurfaceBright.copy(alpha = 0.75f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("hero_next_spotlight_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "Next Featured Channel",
                            tint = SukunaGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NetflixSectionHeader(
    badge: String,
    title: String,
    subtitle: String,
    badgeColor: Color,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor.copy(alpha = 0.2f))
                        .border(1.dp, badgeColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SukunaTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (actionLabel != null && onActionClick != null) {
            Surface(
                onClick = onActionClick,
                shape = RoundedCornerShape(50),
                color = SukunaSurfaceBright,
                border = BorderStroke(1.dp, SukunaBorder)
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = SukunaCyan,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * Netflix signature "Top 10" card with a giant outlined rank numeral behind the channel card.
 */
@Composable
private fun NetflixTop10ChannelCard(
    rank: Int,
    channel: TvChannel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(182.dp)
            .height(145.dp)
            .clickable(onClick = onClick)
            .testTag("top10_card_$rank"),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Giant Netflix-style Rank Number on the left
        Text(
            text = "$rank",
            style = TextStyle(
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Black,
                fontSize = 104.sp,
                color = SukunaSurfaceBright,
                drawStyle = Stroke(width = 6f, join = StrokeJoin.Round)
            ),
            color = if (isSelected) NetflixRed else SukunaCyan.copy(alpha = 0.65f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-4).dp, y = 12.dp)
        )

        // Elevated Channel Card on the right
        Card(
            modifier = Modifier
                .width(136.dp)
                .height(135.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) SukunaSurfaceBright else SukunaSurface
            ),
            border = BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) NetflixRed else SukunaBorder
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                categoryColorPair(channel.category, channel.id).first.copy(alpha = 0.25f),
                                SukunaSurface
                            )
                        )
                    )
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    ChannelAvatarIcon(
                        channel = channel,
                        isSelected = isSelected,
                        isPlaying = isSelected,
                        size = 48.dp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NetflixRed)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "TOP #$rank",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }
                }

                Column {
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
                        text = "${channel.flagEmoji} ${channel.country} • ${channel.category.badgeText}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SukunaTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Horizontal Netflix-style 16:9 Live TV card with live badge, logo from assets/logo/, and quick logo edit icon.
 */
@Composable
private fun NetflixLiveChannelWideCard(
    channel: TvChannel,
    isSelected: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onEditLogoClick: () -> Unit
) {
    val (primaryColor, secondaryColor) = categoryColorPair(channel.category, channel.id)

    Card(
        modifier = Modifier
            .width(210.dp)
            .height(136.dp)
            .clickable(onClick = onClick)
            .testTag("wide_channel_card_${channel.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SukunaSurface),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) NetflixRed else SukunaBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.28f),
                            secondaryColor.copy(alpha = 0.18f),
                            SukunaSurface
                        )
                    )
                )
                .padding(12.dp)
        ) {
            // Subtle watermark category icon
            Icon(
                imageVector = categoryIcon(channel.category),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.07f),
                modifier = Modifier
                    .size(76.dp)
                    .align(Alignment.BottomEnd)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    ChannelAvatarIcon(
                        channel = channel,
                        isSelected = isSelected,
                        isPlaying = isSelected,
                        size = 50.dp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Quick Change Logo from File icon button
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(SukunaObsidian.copy(alpha = 0.72f))
                                .clickable { onEditLogoClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "Change ${channel.name} logo file",
                                tint = SukunaCyan,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NetflixRed)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = channel.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${channel.flagEmoji} ${channel.country} • ${channel.category.label}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SukunaTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (channel.streams.size > 1) {
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
}

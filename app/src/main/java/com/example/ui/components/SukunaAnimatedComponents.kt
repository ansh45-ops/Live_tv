package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ChannelCategory
import com.example.data.TvChannel
import com.example.ui.theme.SukunaCrimson
import com.example.ui.theme.SukunaCyan
import com.example.ui.theme.SukunaEmerald
import com.example.ui.theme.SukunaGold
import com.example.ui.theme.SukunaObsidian
import com.example.ui.theme.SukunaPurple
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun categoryIcon(category: ChannelCategory): ImageVector = when (category) {
    ChannelCategory.ALL -> Icons.Filled.Public
    ChannelCategory.NEWS -> Icons.Filled.Newspaper
    ChannelCategory.ENTERTAINMENT -> Icons.Filled.LiveTv
    ChannelCategory.MOVIES -> Icons.Filled.Movie
    ChannelCategory.MUSIC -> Icons.Filled.MusicNote
    ChannelCategory.SPORTS -> Icons.Filled.SportsSoccer
    ChannelCategory.KIDS -> Icons.Filled.ChildCare
    ChannelCategory.LIFESTYLE -> Icons.Filled.Restaurant
    ChannelCategory.RELIGIOUS -> Icons.Filled.SelfImprovement
    ChannelCategory.SCIENCE -> Icons.Filled.Science
}

fun categoryColorPair(category: ChannelCategory, channelId: Int): Pair<Color, Color> {
    return when (category) {
        ChannelCategory.NEWS -> Color(0xFFFF1E56) to Color(0xFF8F002C)
        ChannelCategory.SPORTS -> Color(0xFF00E676) to Color(0xFF007B5E)
        ChannelCategory.MUSIC -> Color(0xFF00F0FF) to Color(0xFF5A189A)
        ChannelCategory.MOVIES -> Color(0xFFFFB800) to Color(0xFFD00000)
        ChannelCategory.KIDS -> Color(0xFFFF4D94) to Color(0xFF7B2CBF)
        ChannelCategory.LIFESTYLE -> Color(0xFFFF7B00) to Color(0xFF9D4EDD)
        ChannelCategory.RELIGIOUS -> Color(0xFF06D6A0) to Color(0xFF118AB2)
        ChannelCategory.SCIENCE -> Color(0xFF00BBF9) to Color(0xFF3A0CA3)
        else -> {
            val palettes = listOf(
                Color(0xFFFF1E56) to Color(0xFF480CA8),
                Color(0xFF00F0FF) to Color(0xFF1D3557),
                Color(0xFFF72585) to Color(0xFF3A0CA3),
                Color(0xFFFF9E00) to Color(0xFF9D0208)
            )
            palettes[kotlin.math.abs(channelId) % palettes.size]
        }
    }
}

/**
 * Animated ambient canvas background with Sukuna crimson & cyber cyan energy slashes and particles.
 */
@Composable
fun SukunaEnergyBackground(
    isDomainActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sukuna_bg")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isDomainActive) 4500 else 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.22f,
        targetValue = if (isDomainActive) 0.65f else 0.38f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Top-right crimson nebula
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SukunaCrimson.copy(alpha = glowPulse * 0.45f),
                    Color.Transparent
                ),
                center = Offset(w * 0.85f, h * 0.12f),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(w * 0.85f, h * 0.12f)
        )

        // Bottom-left cyan aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SukunaCyan.copy(alpha = glowPulse * 0.32f),
                    Color.Transparent
                ),
                center = Offset(w * 0.15f, h * 0.78f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.15f, h * 0.78f)
        )

        // Dynamic anime energy wave lines
        val wavePath1 = Path()
        val wavePath2 = Path()
        val steps = 40
        for (i in 0..steps) {
            val x = (w / steps) * i
            val norm = i.toFloat() / steps
            val y1 = h * 0.25f + sin(norm * 4f + phase) * 32f
            val y2 = h * 0.68f + cos(norm * 3.5f - phase) * 28f
            if (i == 0) {
                wavePath1.moveTo(x, y1)
                wavePath2.moveTo(x, y2)
            } else {
                wavePath1.lineTo(x, y1)
                wavePath2.lineTo(x, y2)
            }
        }

        drawPath(
            path = wavePath1,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    SukunaCrimson.copy(alpha = if (isDomainActive) 0.45f else 0.18f),
                    SukunaCyan.copy(alpha = if (isDomainActive) 0.45f else 0.16f),
                    Color.Transparent
                )
            ),
            style = Stroke(width = if (isDomainActive) 3.5f else 2f, cap = StrokeCap.Round)
        )

        drawPath(
            path = wavePath2,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    SukunaCyan.copy(alpha = if (isDomainActive) 0.38f else 0.14f),
                    SukunaPurple.copy(alpha = if (isDomainActive) 0.38f else 0.14f),
                    Color.Transparent
                )
            ),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
    }
}

/**
 * Custom rich monogram & icon badge so every single channel has a striking, animated icon
 * even when logo_url is null in the JSON schema.
 */
@Composable
fun ChannelAvatarIcon(
    channel: TvChannel,
    isSelected: Boolean,
    isPlaying: Boolean,
    size: Dp = 54.dp,
    modifier: Modifier = Modifier
) {
    val (primaryColor, secondaryColor) = categoryColorPair(channel.category, channel.id)
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_ring")
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_rot"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (isSelected) 1.06f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 400f),
        label = "avatar_scale"
    )

    val initials = channel.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
        .ifEmpty { "TV" }

    Box(
        modifier = modifier
            .size(size)
            .scale(scaleAnim),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                rotate(ringRotation) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(
                                SukunaCrimson,
                                SukunaCyan,
                                SukunaGold,
                                SukunaCrimson
                            )
                        ),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(if (isSelected) size - 6.dp else size)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = if (isSelected) 0.5f else 0.18f),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Subtle fallback monogram behind the logo file
            Text(
                text = initials,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = (size.value * 0.30f).sp,
                    color = Color.White.copy(alpha = 0.35f)
                )
            )

            // Every channel loads its dedicated logo from app/src/main/assets/logo/<slug>.png
            // or a user-replaced file override!
            AsyncImage(
                model = channel.effectiveLogoUri,
                contentDescription = "${channel.name} logo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            // Corner category mini badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(3.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(SukunaObsidian.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(channel.category),
                    contentDescription = channel.category.label,
                    tint = if (isSelected && isPlaying) SukunaCyan else Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

/**
 * Animated 5-bar audio equalizer for active live stream playback.
 */
@Composable
fun AnimatedEqualizerBars(
    isPlaying: Boolean,
    tint: Color = SukunaCrimson,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_bars")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = if (isPlaying) 1f else 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(380, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = if (isPlaying) 0.95f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(520, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isPlaying) 1f else 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(310, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b3"
    )
    val bar4 by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = if (isPlaying) 0.85f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(460, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b4"
    )

    Row(
        modifier = modifier.height(16.dp),
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        listOf(bar1, bar2, bar3, bar4).forEachIndexed { idx, fraction ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((16f * fraction).dp.coerceAtLeast(3.dp))
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (idx % 2 == 0) tint else SukunaCyan)
            )
        }
    }
}

/**
 * Pulsing crimson LIVE indicator pill.
 */
@Composable
fun PulsingLiveBadge(
    isBuffering: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (isBuffering) SukunaGold.copy(alpha = 0.2f)
                else SukunaCrimson.copy(alpha = 0.22f)
            )
            .border(
                width = 1.dp,
                color = if (isBuffering) SukunaGold else SukunaCrimson,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(
                    (if (isBuffering) SukunaGold else SukunaCrimson).copy(alpha = dotAlpha)
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isBuffering) "SYNCING HLS" else "LIVE STREAM",
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isBuffering) SukunaGold else Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * Verified HTTP 200/206 or real-time ping latency badge.
 */
@Composable
fun StreamLatencyBadge(
    latencyMs: Long?,
    isAlive: Boolean?,
    modifier: Modifier = Modifier
) {
    val badgeColor = when {
        isAlive == true && (latencyMs ?: 999) < 600 -> SukunaEmerald
        isAlive == true -> SukunaCyan
        isAlive == false -> SukunaCrimson
        else -> SukunaCyan
    }
    val text = when {
        isAlive == true && latencyMs != null -> "${latencyMs}ms • HLS 200"
        isAlive == false -> "OFFLINE / GEO"
        else -> "HLS .M3U8"
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(badgeColor.copy(alpha = 0.14f))
            .border(1.dp, badgeColor.copy(alpha = 0.45f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Bolt,
            contentDescription = null,
            tint = badgeColor,
            modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = badgeColor,
                fontSize = 9.5.sp
            )
        )
    }
}

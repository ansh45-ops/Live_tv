package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SukunaCrimson
import com.example.ui.theme.SukunaCyan
import com.example.ui.theme.SukunaGold
import com.example.ui.theme.SukunaObsidian
import com.example.ui.theme.SukunaSurfaceBright
import com.example.ui.theme.SukunaTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Dramatic Netflix-meets-Sukuna Domain Expansion opening splash sequence.
 * Features a glowing crimson "S" ribbon reveal, animated cursed energy slashes,
 * spectrum light beams, and automatic or tap-to-skip entry into the Netflix-style UI.
 */
@Composable
fun SukunaOpeningSplashOverlay(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoScale = remember { Animatable(0.35f) }
    val logoAlpha = remember { Animatable(0f) }
    val slashProgress = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val spectrumSpread = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_ring")
    val ringAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_angle"
    )

    LaunchedEffect(Unit) {
        launch {
            logoAlpha.animateTo(1f, tween(420, easing = FastOutSlowInEasing))
        }
        launch {
            logoScale.animateTo(
                targetValue = 1.05f,
                animationSpec = spring(dampingRatio = 0.55f, stiffness = 220f)
            )
            logoScale.animateTo(1.0f, tween(350))
        }
        launch {
            delay(200)
            slashProgress.animateTo(1f, tween(650, easing = FastOutSlowInEasing))
        }
        launch {
            delay(450)
            subtitleAlpha.animateTo(1f, tween(500))
        }
        launch {
            delay(700)
            spectrumSpread.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
        }
        delay(2750)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050408))
            .clickable { onFinished() }
            .testTag("sukuna_opening_splash"),
        contentAlignment = Alignment.Center
    ) {
        // Subtle background hero art
        Image(
            painter = painterResource(id = R.drawable.img_sukuna_movie_poster_1791663014921),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.24f),
            contentScale = ContentScale.Crop
        )

        // Netflix Spectrum Beams + Sukuna Energy Slashes Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Crimson & Cyan radial core burst
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        SukunaCrimson.copy(alpha = 0.42f * logoAlpha.value),
                        SukunaCyan.copy(alpha = 0.18f * spectrumSpread.value),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = w * (0.45f + 0.35f * spectrumSpread.value)
                ),
                radius = w * (0.45f + 0.35f * spectrumSpread.value),
                center = Offset(cx, cy)
            )

            // Netflix-inspired vertical spectrum light bars expanding outward
            val barColors = listOf(
                SukunaCrimson,
                Color(0xFFFF0055),
                SukunaCyan,
                SukunaGold,
                SukunaCrimson
            )
            val totalBars = 14
            for (i in 0 until totalBars) {
                val offsetFactor = (i - totalBars / 2f) / (totalBars / 2f)
                val xPos = cx + offsetFactor * (w * 0.48f * spectrumSpread.value)
                val color = barColors[i % barColors.size]
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            color.copy(alpha = 0.25f * spectrumSpread.value),
                            Color.Transparent
                        )
                    ),
                    start = Offset(xPos, cy - h * 0.38f),
                    end = Offset(xPos, cy + h * 0.38f),
                    strokeWidth = 6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Diagonal Sukuna "Dismantle & Cleave" Cursed Energy Slashes
            val sp = slashProgress.value
            if (sp > 0f) {
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, SukunaCyan, Color.White, SukunaCrimson, Color.Transparent)
                    ),
                    start = Offset(w * 0.1f, h * 0.32f),
                    end = Offset(w * (0.1f + 0.8f * sp), h * (0.32f + 0.36f * sp)),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, SukunaCrimson, Color.White, SukunaCyan, Color.Transparent)
                    ),
                    start = Offset(w * 0.9f, h * 0.30f),
                    end = Offset(w * (0.9f - 0.8f * sp), h * (0.30f + 0.38f * sp)),
                    strokeWidth = 3.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Center Animated Emblem & Typography
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .scale(logoScale.value)
                .alpha(logoAlpha.value)
        ) {
            Box(
                modifier = Modifier.size(124.dp),
                contentAlignment = Alignment.Center
            ) {
                // Rotating outer energy ring
                Canvas(modifier = Modifier.fillMaxSize()) {
                    rotate(ringAngle) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    SukunaCrimson,
                                    SukunaCyan,
                                    Color.White,
                                    SukunaCrimson
                                )
                            ),
                            style = Stroke(width = 4.dp.toPx())
                        )
                    }
                }

                // Netflix-Red "S" Emblem Core
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE50914),
                                    Color(0xFF8B001D),
                                    SukunaObsidian
                                )
                            )
                        )
                        .border(2.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 62.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.LiveTv,
                        contentDescription = null,
                        tint = SukunaCyan,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Netflix-Style Bold Wordmark
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "SUKUNA",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 36.sp,
                        letterSpacing = 5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFE50914)
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SukunaCyan)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LIVE TV",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            color = SukunaObsidian
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(subtitleAlpha.value)
            ) {
                Text(
                    text = "DOMAIN EXPANSION • CINEMA & LIVE IPTV ENGINE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SukunaCyan,
                        letterSpacing = 2.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Free Movie Streaming APIs • 87+ Verified BD, IN & Global Channels",
                    style = MaterialTheme.typography.bodySmall,
                    color = SukunaTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(22.dp))

                LinearProgressIndicator(
                    color = Color(0xFFE50914),
                    trackColor = SukunaSurfaceBright,
                    modifier = Modifier
                        .width(190.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Bottom Skip Pill
        Surface(
            onClick = onFinished,
            shape = RoundedCornerShape(50),
            color = SukunaSurfaceBright.copy(alpha = 0.85f),
            border = androidx.compose.foundation.BorderStroke(1.dp, SukunaCyan.copy(alpha = 0.5f)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .testTag("skip_splash_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Bolt,
                    contentDescription = null,
                    tint = SukunaCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Enter Sukuna Netflix Theater",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }
        }
    }
}

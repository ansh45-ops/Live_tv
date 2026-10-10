package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val SukunaDarkColorScheme = darkColorScheme(
    primary = SukunaCrimson,
    onPrimary = SukunaTextPrimary,
    primaryContainer = SukunaCrimsonDark,
    onPrimaryContainer = SukunaTextPrimary,
    secondary = SukunaCyan,
    onSecondary = SukunaObsidian,
    secondaryContainer = SukunaCyanDark,
    onSecondaryContainer = SukunaTextPrimary,
    tertiary = SukunaGold,
    onTertiary = SukunaObsidian,
    background = SukunaObsidian,
    onBackground = SukunaTextPrimary,
    surface = SukunaSurface,
    onSurface = SukunaTextPrimary,
    surfaceVariant = SukunaSurfaceBright,
    onSurfaceVariant = SukunaTextSecondary,
    outline = SukunaBorder,
    outlineVariant = SukunaSurfaceElevated
)

val SukunaShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SukunaDarkColorScheme,
        typography = Typography,
        shapes = SukunaShapes,
        content = content
    )
}

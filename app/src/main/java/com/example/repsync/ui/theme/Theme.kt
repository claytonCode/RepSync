package com.example.repsync.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan

private val DarkColorScheme = darkColorScheme(
    primary = Lime,
    onPrimary = White,

    secondary = NeonLime,
    onSecondary = Midnight,

    tertiary = Cyan,
    onTertiary = Midnight,

    background = Midnight,
    onBackground = White,

    surface = DeepNavy,
    onSurface = White,

    surfaceVariant = Color(0xFF151B32),
    onSurfaceVariant = SoftWhite
)

private val LightColorScheme = lightColorScheme(
    primary = Lime,
    onPrimary = White,

    secondary = NeonLime,
    onSecondary = Midnight,

    tertiary = Cyan,
    onTertiary = Midnight,

    background = Color(0xFFF4F7FB),
    onBackground = Midnight,

    surface = White,
    onSurface = Midnight,

    surfaceVariant = Color(0xFFE8EEF8),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun RepSyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
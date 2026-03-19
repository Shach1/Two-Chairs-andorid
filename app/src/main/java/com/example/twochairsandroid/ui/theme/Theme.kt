package com.example.twochairsandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SkyPrimary,
    secondary = LimeAccent,
    tertiary = PinkAccent,
    background = SkySurface,
    surface = SkySurface,
    onPrimary = InkPrimary,
    onBackground = WhiteSoft,
    onSurface = WhiteSoft,
)

private val LightColorScheme = lightColorScheme(
    primary = SkyPrimary,
    secondary = LimeAccent,
    tertiary = PinkAccent,
    background = SkySurface,
    surface = SkySurface,
    onPrimary = InkPrimary,
    onBackground = InkPrimary,
    onSurface = InkPrimary,
)

@Composable
fun TwoChairsAndroidTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

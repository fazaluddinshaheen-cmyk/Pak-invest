package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PakEmeraldGreen,
    onPrimary = PakBlackBg,
    primaryContainer = PakEmeraldContainer,
    onPrimaryContainer = PakOnEmeraldContainer,

    secondary = PakGold,
    onSecondary = PakBlackBg,
    secondaryContainer = PakGoldContainer,
    onSecondaryContainer = PakOnGoldContainer,

    tertiary = PakEmeraldLight,
    onTertiary = PakBlackBg,

    background = PakBlackBg,
    onBackground = PakTextPrimary,

    surface = PakDarkSurface,
    onSurface = PakTextPrimary,
    surfaceVariant = PakDarkSurfaceVariant,
    onSurfaceVariant = PakTextSecondary,

    outline = PakDarkBorder,
    error = PakError,
    onError = PakWhite
)

private val LightColorScheme = darkColorScheme(
    primary = PakEmeraldGreen,
    onPrimary = PakBlackBg,
    primaryContainer = PakEmeraldContainer,
    onPrimaryContainer = PakOnEmeraldContainer,
    secondary = PakGold,
    background = PakBlackBg,
    surface = PakDarkSurface,
    onSurface = PakTextPrimary
)

@Composable
fun PakInvestmentTheme(
    darkTheme: Boolean = true, // We default to the requested Green & Black / Gold theme
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = PakBlackBg.toArgb()
            window.navigationBarColor = PakBlackBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

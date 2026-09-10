package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = CivicPrimary,
    onPrimary = CivicOnPrimary,
    primaryContainer = CivicPrimaryContainer,
    onPrimaryContainer = CivicOnPrimaryContainer,
    secondary = CivicSecondary,
    onSecondary = CivicOnSecondary,
    secondaryContainer = CivicSecondaryContainer,
    onSecondaryContainer = CivicOnSecondaryContainer,
    tertiary = CivicTertiary,
    onTertiary = CivicOnTertiary,
    tertiaryContainer = CivicTertiaryContainer,
    onTertiaryContainer = CivicOnTertiaryContainer,
    background = CivicSurface,
    onBackground = CivicOnSurface,
    surface = CivicSurface,
    onSurface = CivicOnSurface,
    surfaceVariant = CivicSurfaceContainerHighest,
    onSurfaceVariant = CivicOnSurfaceVariant,
    surfaceContainerLowest = CivicSurfaceContainerLowest,
    surfaceContainerLow = CivicSurfaceContainerLow,
    surfaceContainer = CivicSurfaceContainer,
    surfaceContainerHigh = CivicSurfaceContainerHigh,
    surfaceContainerHighest = CivicSurfaceContainerHighest,
    inverseSurface = CivicInverseSurface,
    inverseOnSurface = CivicInverseOnSurface,
    outline = CivicOutline,
    outlineVariant = CivicOutlineVariant,
    error = CivicError,
    onError = CivicOnError,
    errorContainer = CivicErrorContainer,
    onErrorContainer = CivicOnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = CivicPrimaryContainer,
    onPrimary = CivicOnPrimary,
    primaryContainer = CivicPrimary,
    onPrimaryContainer = CivicOnPrimaryContainer,
    secondary = CivicSecondaryContainer,
    onSecondary = CivicOnSecondary,
    secondaryContainer = CivicSecondary,
    onSecondaryContainer = CivicOnSecondaryContainer,
    tertiary = CivicTertiaryFixedDim,
    onTertiary = CivicOnTertiaryFixed,
    tertiaryContainer = CivicTertiaryContainer,
    onTertiaryContainer = CivicOnTertiaryContainer,
    background = CivicInverseSurface,
    onBackground = CivicInverseOnSurface,
    surface = CivicInverseSurface,
    onSurface = CivicInverseOnSurface,
    outline = CivicOutlineVariant,
    outlineVariant = CivicOutline,
    error = CivicError,
    onError = CivicOnError
)

@Composable
fun CivicPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

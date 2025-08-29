package com.fashionshop.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = PureWhite,
    secondary = BlueDark,
    onSecondary = PureWhite,
    background = PureWhite,
    surface = PureWhite,
    onBackground = Jet,
    onSurface = Jet,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDark,
)

private val DarkColors = darkColorScheme(
    primary = BluePrimary,
    onPrimary = PureWhite,
    secondary = BlueDark,
    onSecondary = PureWhite,
    background = Onyx,
    surface = Onyx,
    onBackground = Snow,
    onSurface = Snow,
    primaryContainer = BlueDark,
    onPrimaryContainer = PureWhite,
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(28.dp)
)

@Composable
fun FashionShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}

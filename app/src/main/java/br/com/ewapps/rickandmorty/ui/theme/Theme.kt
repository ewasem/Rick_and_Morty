package br.com.ewapps.rickandmorty.ui.theme

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

private val DarkColorPalette = darkColors(
    primary = Color1,
    primaryVariant = Color2,
    secondary = Color3
)

private val LightColorPalette = lightColors(
    primary = Color1,
    primaryVariant = Color2,
    secondary = Color3,


    background = ColorBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color3,
    onBackground = Color4,
    onSurface = Color2,


)

@Composable
fun RickAndMortyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

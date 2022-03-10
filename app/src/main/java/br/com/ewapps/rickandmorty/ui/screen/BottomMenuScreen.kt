package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuScreen(val route: String, val icon: ImageVector, val title: String) {
    object Characters: BottomMenuScreen("Characters", Icons.Outlined.Person, "Personagens")
    object Episodes: BottomMenuScreen("Episodes", Icons.Outlined.DesktopWindows, "Episódios")
    object Locations: BottomMenuScreen("Locations", Icons.Outlined.LocationOn, "Lugares")

}

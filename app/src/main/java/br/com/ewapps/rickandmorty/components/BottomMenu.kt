package br.com.ewapps.rickandmorty.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.ewapps.rickandmorty.ui.screen.BottomMenuScreen

@Composable
fun BottomMenu(navController: NavController) {
    val menuItens = listOf(BottomMenuScreen.Characters, BottomMenuScreen.Episodes, BottomMenuScreen.Locations)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (currentRoute == BottomMenuScreen.Characters.route || currentRoute == BottomMenuScreen.Episodes.route || currentRoute == BottomMenuScreen.Locations.route) {
    BottomNavigation(contentColor = Color.White) {
        menuItens.forEach { it ->
            BottomNavigationItem(label = { Text(text = it.title) },
                alwaysShowLabel = true, selectedContentColor = Color.White, unselectedContentColor = Color.Gray, selected = currentRoute == it.route,
                onClick = {
                          navController.navigate(it.route) {
                              navController.graph.startDestinationId?.let {
                                  route ->
                                  popUpTo(route) {
                                      saveState = true
                                  }
                              }
                              launchSingleTop = true
                              restoreState = true
                          }
                }, icon = {Icon(
                    imageVector = it.icon,
                    contentDescription = it.title)

                })
        }
    }
}}
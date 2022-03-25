package br.com.ewapps.rickandmorty.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.ewapps.rickandmorty.ui.screen.BottomMenuScreen
import br.com.ewapps.rickandmorty.ui.theme.Color2
import br.com.ewapps.rickandmorty.ui.theme.Color4
import br.com.ewapps.rickandmorty.ui.theme.ColorBackground

@Composable
fun BottomMenu(navController: NavController, bottomBarState: MutableState<Boolean>) {
    val menuItens =
        listOf(BottomMenuScreen.Characters, BottomMenuScreen.Episodes, BottomMenuScreen.Locations)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(contentColor = Color.White) {
            menuItens.forEach { it ->
                BottomNavigationItem(label = { Text(text = it.title) },
                    alwaysShowLabel = true,
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
                    selected = currentRoute == it.route,
                    onClick = {
                        navController.navigate(it.route) {
                            navController.graph.startDestinationId?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.title
                        )

                    })
            }
        }
    }

}

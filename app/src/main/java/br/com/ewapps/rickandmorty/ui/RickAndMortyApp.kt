package br.com.ewapps.rickandmorty.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.ewapps.rickandmorty.components.BottomMenu
import br.com.ewapps.rickandmorty.ui.screen.*

@Composable
fun RickAndMortyApp() {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    MainScreen(navController = navController, scrollState = scrollState)
}

@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {
    Scaffold(bottomBar = {
        BottomMenu(navController = navController)
    }) {

            padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Navigation(navController = navController)
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "Characters") {
        bottomNavigation(navController = navController)
        composable("Characters") {
            Characters(navController = navController)
        }

        composable("CharacterDetailScreen/{characterId}", arguments = listOf(navArgument("characterId"){type = NavType.IntType})) {
            val id = it.arguments?.getInt("characterId")
            val characterData = id?.let { it1 -> MockData.getCharacterData(it1) }
            if (characterData != null) {
                CharacterDetailScreen(navController, characterData)
            }
        }
        
        composable("Episodes") {
            Episodes(navController = navController)
        }

        composable("Locations") {
            Locations(navController = navController)
        }
    }
}

fun NavGraphBuilder.bottomNavigation(navController: NavController) {
    composable(BottomMenuScreen.Characters.route) {
        Characters(navController = navController)
    }
    composable(BottomMenuScreen.Episodes.route) {
        Episodes(navController = navController)
    }
    composable(BottomMenuScreen.Locations.route) {
        Locations(navController = navController)
    }
}

package br.com.ewapps.rickandmorty.ui

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.ewapps.rickandmorty.components.BottomMenu
import br.com.ewapps.rickandmorty.models.CharacterModel
import br.com.ewapps.rickandmorty.network.AppManager
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
fun Navigation(navController: NavHostController, appManager: AppManager = AppManager()) {
    val characters = appManager.characterResponse.value.result
    Log.d("Personagens: ", "$characters")
    characters?.let {
        NavHost(navController = navController, startDestination = "Characters") {
            bottomNavigation(navController = navController, characters)
            composable("Characters") {
                Characters(navController = navController, characters)
            }

            composable("CharacterDetailScreen/{index}", arguments = listOf(navArgument("index"){type = NavType.IntType})) {
                val index = it.arguments?.getInt("index")
                index.let {
                    val character = characters[index!!]
                    CharacterDetailScreen(navController, characterData = character)
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

}

fun NavGraphBuilder.bottomNavigation(navController: NavController, characters: List<CharacterModel>) {
    composable(BottomMenuScreen.Characters.route) {
        Characters(navController = navController, characters = characters)
    }
    composable(BottomMenuScreen.Episodes.route) {
        Episodes(navController = navController)
    }
    composable(BottomMenuScreen.Locations.route) {
        Locations(navController = navController)
    }
}

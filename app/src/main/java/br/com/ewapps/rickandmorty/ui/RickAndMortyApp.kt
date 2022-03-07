package br.com.ewapps.rickandmorty.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.ewapps.rickandmorty.ui.screen.CharacterDetailScreen
import br.com.ewapps.rickandmorty.ui.screen.Characters

@Composable
fun RickAndMortyApp() {
    Navigation()
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Characters") {
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
    }
}
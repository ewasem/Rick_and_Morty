package br.com.ewapps.rickandmorty.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.ewapps.rickandmorty.components.BottomMenu
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.ui.screen.*

@Composable
fun RickAndMortyApp(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    MainScreen(navController = navController, scrollState = scrollState, viewModel = viewModel)
}

@Composable
fun MainScreen(
    navController: NavHostController,
    scrollState: ScrollState,
    viewModel: MainViewModel
) {
    Scaffold(bottomBar = {
        BottomMenu(navController = navController)
    }) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Navigation(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isError.collectAsState()
    val totalCharacters by viewModel.infoResponse.collectAsState()
    val teste = viewModel.result.collectAsState(null).value

    //val charactersState by viewModel.characterList.collectAsState()

    val characters = teste?.collectAsState()?.value?.result

    NavHost(navController = navController, startDestination = "Characters") {
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)
        bottomNavigation(navController = navController, characters, totalCharacters.info?.count, viewModel, isLoading = isLoading, isError = isError)

        composable(
            "CharacterDetailScreen/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) {
            val index = it.arguments?.getInt("index")
            index.let {
                val character = characters?.get(index!!)
                if (character != null) {
                    CharacterDetailScreen(navController, characterData = character)
                }
            }
        }
    }

}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    characters: List<Character>?,
    totalCharacters: Int?,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    composable(BottomMenuScreen.Characters.route) {
        Characters(navController = navController, characters = characters, totalCharacters, viewModel, isLoading, isError)
    }
    composable(BottomMenuScreen.Episodes.route) {
        Episodes(navController = navController)
    }
    composable(BottomMenuScreen.Locations.route) {
        Locations(navController = navController)
    }
}

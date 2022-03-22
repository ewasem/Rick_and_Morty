package br.com.ewapps.rickandmorty.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.ewapps.rickandmorty.components.BottomMenu
import br.com.ewapps.rickandmorty.models.*
import br.com.ewapps.rickandmorty.ui.screen.*
import kotlinx.coroutines.flow.forEach

@Composable
fun RickAndMortyApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    MainScreen(navController = navController, viewModel = viewModel)
}

@Composable
fun MainScreen(
    navController: NavHostController,
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
    val splash by viewModel.splash.collectAsState()
    val error by viewModel.isError.collectAsState()
    val totalCharacters by viewModel.infoResponse.collectAsState()
    val characterResponse = viewModel.resultCharacterList.collectAsState(null).value

    val characters = characterResponse?.collectAsState()?.value?.result

    val allEpisodes = viewModel.allEpisodes.collectAsState(initial = null).value
    val allEpisodeList = allEpisodes?.collectAsState()?.value
    val episodeSelectedData = viewModel.episodeSelectedData.collectAsState().value
    val episodeCharacters = viewModel.episodeCharacters.collectAsState().value

    NavHost(navController = navController, startDestination = if (splash) "SplashScreen" else "Characters") {
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)

        bottomNavigation(
            navController = navController,
            characters,
            totalCharacters.info?.count,
            viewModel,
            isLoading = isLoading,
            isError = isError,
            episodeList = allEpisodeList
        )

        composable(
            "CharacterDetailScreen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {

            val id = it.arguments?.getInt("id")
            viewModel.selectedCharacter(id!!)
            CharacterDetailScreen(navController, viewModel)

        }


        composable(
            "EpisodeDetailScreen/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType })
        ) {

            val id = it.arguments?.getInt("id")
            viewModel.selectedEpisode(id!!)

                EpisodeDetailScreen(
                    viewModel = viewModel,
                    navController = navController,
                    episode = episodeSelectedData,
                    isError = isError,
                    isLoading = isLoading,
                    characterList = episodeCharacters
                )
        }

        composable("SplashScreen") {

            SplashScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    characters: List<Character>?,
    totalCharacters: Int?,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>,
    episodeList: List<SeasonTmdb>?
) {
    composable(BottomMenuScreen.Characters.route) {


        Characters(
            navController = navController,
            characters = characters,
            totalCharacters,
            viewModel,
            isLoading,
            isError
        )


    }
    composable(BottomMenuScreen.Episodes.route) {
        Episodes(navController = navController, episodeList = episodeList)
    }
    composable(BottomMenuScreen.Locations.route) {
        Locations(navController = navController)
    }
}


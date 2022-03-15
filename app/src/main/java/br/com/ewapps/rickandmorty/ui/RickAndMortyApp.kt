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
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.models.Episode
import br.com.ewapps.rickandmorty.models.Season
import br.com.ewapps.rickandmorty.ui.screen.*

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
    val error by viewModel.isError.collectAsState()
    val totalCharacters by viewModel.infoResponse.collectAsState()
    val characterResponse = viewModel.resultCharacterList.collectAsState(null).value

    val episodeList = viewModel.episodeList.collectAsState().value.result
    val allEpisode = viewModel.resultEpisodeList.collectAsState(null).value
    val episodes = allEpisode?.collectAsState()?.value
    val characters = characterResponse?.collectAsState()?.value?.result
    val tmdbData = viewModel.tmdbData.collectAsState(null).value

    NavHost(navController = navController, startDestination = "Characters") {
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)

        bottomNavigation(
            navController = navController,
            characters,
            totalCharacters.info?.count,
            viewModel,
            isLoading = isLoading,
            isError = isError,
            episodeList = episodes
        )

        composable(
            "CharacterDetailScreen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id")
            id.let {
                var character = characters!![0]
                val list = mutableListOf<Episode>()
                //Procura o personagem pelo id
                characters.forEach {
                    if (it.id == id) {
                        character = it
                        val episodesForSeason = character.episode

                        //pega a lista de episódios do personagem
                        episodesForSeason.forEach {
                            val episodeId = it.substringAfterLast("/").toInt()
                            viewModel.episodeList.value.result?.forEach {
                                if (it.id == episodeId) {
                                    list.add(it)
                                }
                            }
                        }
                    }
                }
                val seasonEpisodes = viewModel.getEpisodesinSeasonFormat(list = list)

                CharacterDetailScreen(navController, characterData = character, seasonEpisodes)
            }
        }

        composable(
            "EpisodeDetailScreen/{id}/{season}/{episode}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("season") { type = NavType.StringType },
                navArgument("episode") { type = NavType.StringType })
        ) {

            val id = it.arguments?.getInt("id")
            val season = it.arguments?.getString("season")
            println("Temporada: $season")

            val episode = it.arguments?.getString("episode")
            println("Temporada epi: $episode")
                    viewModel.getTmdbData(season = season!!, episode = episode!!)

            //Procura o episódio pelo id
            id.let {
                var episode: Episode = episodeList!![0]
                episodeList.forEach {
                    if (it.id == id) {
                        episode = it
                    }
                }
                EpisodeDetailScreen(
                    viewModel = viewModel,
                    episode = episode,
                    navController = navController,
                    tmdbData = tmdbData!!.collectAsState().value,
                    isError = isError,
                    isLoading = isLoading
                )
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
    isError: MutableState<Boolean>,
    episodeList: List<Season>?
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

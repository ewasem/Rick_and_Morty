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
    val error by viewModel.isError.collectAsState()
    val totalCharacters by viewModel.infoResponse.collectAsState()
    val characterResponse = viewModel.resultCharacterList.collectAsState(null).value

    val characters = characterResponse?.collectAsState()?.value?.result

    val allEpisodes = viewModel.allEpisodes.collectAsState(initial = null).value
    val allEpisodeList = allEpisodes?.collectAsState()?.value

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
            episodeList = allEpisodeList
        )

        composable(
            "CharacterDetailScreen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val list = mutableListOf<EpisodeTmdb>()
            val seasonEpisodes = mutableListOf<SeasonTmdb>()
            val id = it.arguments?.getInt("id")
            id.let {
                var character = characters!![0]
                //Procura o personagem pelo id
                characters.forEach {
                    if (it.id == id) {
                        character = it
                        val characterEpisodes = character.episode

                        //pega a lista de episódios do personagem
                        characterEpisodes.forEach {
                            val episodeId = it.substringAfterLast("/").toInt()
                            allEpisodeList?.forEach {
                                it.episodes?.forEach {
                                    if (it.episodeId == episodeId) {
                                        list.add(it)
                                    }
                                }
                            }
                        }
                        var season = 0
                        var listEpisodesPerSeason = mutableListOf<EpisodeTmdb>()
                        for (i in list.indices) {

                            if (season == list[i].seasonNumber) {
                                listEpisodesPerSeason.add(list[i])
                            } else {
                                if (listEpisodesPerSeason.isNotEmpty()) {
                                    allEpisodeList!!.forEach {
                                        if(it.seasonNumber == season) {
                                            seasonEpisodes.add(
                                                SeasonTmdb(
                                                    it.air_date,
                                                    listEpisodesPerSeason,
                                                    it.name,
                                                    it.overview,
                                                    it.posterPath,
                                                    it.seasonNumber
                                                )
                                            )
                                        }
                                    }
                                }
                                season = list[i].seasonNumber!!
                                listEpisodesPerSeason = mutableListOf<EpisodeTmdb>()
                                listEpisodesPerSeason.add(list[i])
                            }
                            if (list.lastIndex == i) {
                                if (listEpisodesPerSeason.isNotEmpty()) {
                                    allEpisodeList!!.forEach {
                                        if(it.seasonNumber == season) {
                                            seasonEpisodes.add(
                                                SeasonTmdb(
                                                    it.air_date,
                                                    listEpisodesPerSeason,
                                                    it.name,
                                                    it.overview,
                                                    it.posterPath,
                                                    it.seasonNumber
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                CharacterDetailScreen(navController, characterData = character, seasonEpisodes)
            }
        }


        composable(
            "EpisodeDetailScreen/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType })
        ) {

            val id = it.arguments?.getInt("id")
            val charList = viewModel.getCharactersFromEpisodeStringList(id!!)
            val season = it.arguments?.getInt("season")

            var episodeTmdb = EpisodeTmdb()
            allEpisodeList!!.forEach {
                it.episodes!!.forEach {
                    if (it.episodeId == id) {
                        episodeTmdb = it
                    }
                }

                EpisodeDetailScreen(
                    viewModel = viewModel,
                    navController = navController,
                    episode = episodeTmdb,
                    isError = isError,
                    isLoading = isLoading,
                    characterList = charList
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


package br.com.ewapps.rickandmorty.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import br.com.ewapps.rickandmorty.components.BottomMenu
import br.com.ewapps.rickandmorty.models.*
import br.com.ewapps.rickandmorty.ui.screen.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RickAndMortyApp(viewModel: MainViewModel) {
    val navController = rememberAnimatedNavController()
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val loading by viewModel.isLoading.collectAsState()
    val splash by viewModel.splash.collectAsState()
    val error by viewModel.isError.collectAsState()
    val totalCharacters by viewModel.infoResponse.collectAsState()
    val characterResponse by viewModel.characetrList.collectAsState()
    val allEpisodes by viewModel.allEpisodes.collectAsState()
    val episodeSelectedData by viewModel.episodeSelectedData.collectAsState()
    val episodeCharacters by viewModel.episodeCharacters.collectAsState()

    AnimatedNavHost(
        navController = navController,
        startDestination = if (splash) "SplashScreen" else "Characters"
    ) {
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)
        val totalCharacters1 = mutableStateOf(totalCharacters)
        val characters = mutableStateOf(characterResponse)
        val allEpisodeList = mutableStateOf(allEpisodes)
        bottomNavigation(
            navController = navController,
            characters.value.result,
            totalCharacters1.value.info?.count,
            viewModel,
            isLoading = isLoading,
            isError = isError,
            episodeList = allEpisodeList.value
        )


        composable(
            "CharacterDetailScreen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                ) + fadeIn(
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                ) + fadeOut()
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                ) + fadeIn(
                    animationSpec = tween(1000)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                ) + fadeOut()
            }
        ) {

            val id = it.arguments?.getInt("id")
            viewModel.selectedCharacter(id!!)
            CharacterDetailScreen(navController, viewModel)
        }

        composable(
            "EpisodeDetailScreen/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                ) + fadeIn(
                    animationSpec = tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                ) + fadeOut()
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                ) + fadeIn(
                    animationSpec = tween(1000)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                ) + fadeOut()
            }
        ) {

            val id = it.arguments?.getInt("id")
            viewModel.selectedEpisode(id!!)

            EpisodeDetailScreen(
                viewModel = viewModel,
                navController = navController,
                episode = episodeSelectedData,
                characterList = episodeCharacters
            )
        }

        composable("SplashScreen") {

            SplashScreen(navController = navController)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    characters: List<Character>?,
    totalCharacters: Int?,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>,
    episodeList: List<SeasonTmdb>?
) {
    composable(BottomMenuScreen.Characters.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeIn(
                animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeOut()
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeIn(
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        }) {
        Characters(
            navController = navController,
            characters = characters,
            totalCharacters,
            viewModel,
            isLoading,
            isError
        )

    }
    composable(BottomMenuScreen.Episodes.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeIn(
                animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeOut()
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeIn(
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        }) {
        Episodes(navController = navController, episodeList = episodeList)
    }
    composable(BottomMenuScreen.Locations.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeIn(
                animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeOut()
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeIn(
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        }) {
        Locations(navController = navController)
    }
}


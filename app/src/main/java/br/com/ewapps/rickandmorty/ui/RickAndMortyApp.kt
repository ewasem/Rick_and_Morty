package br.com.ewapps.rickandmorty.ui


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.composable
import br.com.ewapps.rickandmorty.components.BottomMenu
import br.com.ewapps.rickandmorty.components.SearchFeature
import br.com.ewapps.rickandmorty.components.TopBar
import br.com.ewapps.rickandmorty.models.*
import br.com.ewapps.rickandmorty.ui.screen.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

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
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val topBarState = rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        BottomMenuScreen.Characters.route -> {
            topBarState.value = true
            bottomBarState.value = true
            viewModel.changeTopBarTiltle("Personagens")
        }
        BottomMenuScreen.Episodes.route -> {
            topBarState.value = true
            bottomBarState.value = true
            viewModel.changeTopBarTiltle("Episódios")
        }
        BottomMenuScreen.Locations.route -> {
            topBarState.value = true
            bottomBarState.value = true
            viewModel.changeTopBarTiltle("Lugares")
        }
        "CharacterDetailScreen/{id}" -> {
            topBarState.value = true
            bottomBarState.value = false
            viewModel.changeTopBarTiltle("Detalhes do Personagem")
        }
        "EpisodeDetailScreen/{id}" -> {
            topBarState.value = true
            bottomBarState.value = false
            viewModel.changeTopBarTiltle("Detalhes do Episódio")
        }

        else -> {
            bottomBarState.value = false
            topBarState.value = false
            viewModel.changeTopBarTiltle("")
        }

    }

    Scaffold(
        bottomBar = {
            BottomMenu(navController = navController, bottomBarState = bottomBarState)
        },
        topBar = {
            TopBar(
                viewModel = viewModel,
                navController = navController,
                topBarState = topBarState,
                onBackPressed = { navController.popBackStack() }
            )
        }) { padding ->
        Column(
            //modifier = Modifier.padding(padding)
        ) {

            Navigation(navController = navController, viewModel = viewModel, padding= padding)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    padding: PaddingValues
) {
    val loading by viewModel.isLoading.collectAsState()
    val splash by viewModel.splash.collectAsState()
    val error by viewModel.isError.collectAsState()
    val totalCharacters by viewModel.infoResponse.collectAsState()
    val characterResponse by viewModel.characetrList.collectAsState(null)
    val allEpisodes by viewModel.allEpisodes.collectAsState()
    val episodeSelectedData by viewModel.episodeSelectedData.collectAsState()
    val episodeCharacters by viewModel.episodeCharacters.collectAsState()
    val firstTime by viewModel.firsTime.collectAsState()

    AnimatedNavHost(
        navController = navController,
        startDestination = if (splash) "SplashScreen" else "Characters"
    ) {

        val firstTimeValue = mutableStateOf(firstTime)
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)
        val characters = mutableStateOf(characterResponse)
        val totalCharacters1 = mutableStateOf(totalCharacters)
        val allEpisodeList = mutableStateOf(allEpisodes)
        val query = mutableStateOf(viewModel.query.value)
        val filter = mutableStateOf(viewModel.filterType.value)

        bottomNavigation(
            navController = navController,
            characters.value?.value?.result,
            totalCharacters1.value.info?.count,
            viewModel,
            isLoading = isLoading,
            isError = isError,
            episodeList = allEpisodeList.value,
            query = query,
            padding = padding,
            firstTime = firstTimeValue
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
            Column(modifier = Modifier.padding(padding)) {
            CharacterDetailScreen(navController, viewModel)
        }}

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

            Column(modifier = Modifier.padding(padding)) {
            EpisodeDetailScreen(
                viewModel = viewModel,
                navController = navController,
                episode = episodeSelectedData,
                characterList = episodeCharacters
            )
        }}

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
    episodeList: List<SeasonTmdb>?,
    query: MutableState<String>,
    padding: PaddingValues,
    firstTime: MutableState<Boolean>
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
        SearchFeature(query = query, viewModel = viewModel)
        Column(modifier = Modifier.padding(padding)) {
            Characters(
                navController = navController,
                characters = characters,
                totalCharacters,
                viewModel,
                isLoading,
                isError,
                query,
                firstTime
            )
        }
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
        Column(modifier = Modifier.padding(padding)) {
            Episodes(navController = navController, episodeList = episodeList)
        }

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
        Column(modifier = Modifier.padding(padding)) {
            Locations(navController = navController)
        }
    }
}


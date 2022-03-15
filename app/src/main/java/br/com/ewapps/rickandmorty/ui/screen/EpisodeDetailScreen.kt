package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.components.ErrorUI
import br.com.ewapps.rickandmorty.components.LoadingUI
import br.com.ewapps.rickandmorty.models.Episode
import br.com.ewapps.rickandmorty.models.TmdbModel
import br.com.ewapps.rickandmorty.ui.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeDetailScreen(
    viewModel: MainViewModel, episode: Episode, navController: NavController, tmdbData: TmdbModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {

    val characters = viewModel.getCharactersEpisode(episode)
    val season = viewModel.getSeasonFromString(episode.episode)
    val epiString = viewModel.getEpisodeFromString(episode.episode)
    val date = viewModel.dateFormatter(episode.air_date)

    Scaffold(topBar = { EpisodeDetailTopAppBar(onBackPressed = { navController.popBackStack() }) }) {

        when {
            isLoading.value -> {
                LoadingUI()
            }
            isError.value -> {
                ErrorUI()
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    tmdbData.name?.let { it1 ->
                        Text(
                            text = it1,
                            Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Text(text = "Temporada: $season")
                    Text(text = "Episódio: $epiString")
                    Text(text = "Lançamento: $date")
                    tmdbData.overview?.let { it1 ->
                        Text(
                            text = it1,
                            Modifier.padding(top = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = "Personagens:",
                        Modifier.padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    LazyVerticalGrid(
                        cells = GridCells.Adaptive(160.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(characters.size) { index ->

                            CharacterItem(
                                characterData = (characters[index]),
                                onCharacterClicked = {
                                    navController.navigate("CharacterDetailScreen/${it}")
                                })
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun EpisodeDetailTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = "Detalhe do Episódio", fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Botão voltar"
                )
            }
        }, elevation = 8.dp
    )
}

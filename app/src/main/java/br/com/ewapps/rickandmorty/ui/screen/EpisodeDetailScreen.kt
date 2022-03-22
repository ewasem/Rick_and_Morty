package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.components.ErrorUI
import br.com.ewapps.rickandmorty.components.LoadingUI
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.models.EpisodeTmdb
import br.com.ewapps.rickandmorty.ui.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeDetailScreen(
    viewModel: MainViewModel,
    episode: EpisodeTmdb,
    navController: NavController,
    characterList: List<Character>,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {

    val date = episode.air_date?.let { viewModel.dateFormatter(it) }
    val currentOrientation = LocalConfiguration.current.orientation

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
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    episode.name?.let { it1 ->
                        Text(
                            text = it1,
                            Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (date != null) {
                        TextInformation(orientation = currentOrientation, date, episode)
                    }
                    episode.overview?.let { it1 ->
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
                        items(characterList.size) { index ->

                            CharacterItem(
                                characterData = (characterList[index]),
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
        }, elevation = 5.dp
    )
}

@Composable
fun TextInformation(orientation: Int, date: String, episode: EpisodeTmdb) {

    if (orientation == 1) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Temporada: ${episode.seasonNumber}")
            Text(text = "Episódio: ${episode.episodeNumber}")
            Text(text = "Lançamento: $date")
        }
    } else {
        Row() {
            Text(text = "Temporada: ${episode.seasonNumber}", Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(text = "Episódio: ${episode.episodeNumber}", Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(text = "Lançamento: $date", Modifier.weight(1f), textAlign = TextAlign.Center)
        }
    }

}

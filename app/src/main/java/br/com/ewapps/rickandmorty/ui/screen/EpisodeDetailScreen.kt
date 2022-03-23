package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeDetailScreen(
    viewModel: MainViewModel,
    episode: EpisodeTmdb,
    navController: NavController,
    characterList: List<Character>
) {

    val date = episode.air_date?.let { viewModel.dateFormatter(it) }
    val currentOrientation = LocalConfiguration.current.orientation

    Scaffold(topBar = { EpisodeDetailTopAppBar(onBackPressed = { navController.popBackStack() }) }) {

        if (date != null) {
            if (currentOrientation == 1) {
                Portrait(
                    navController = navController,
                    date = date,
                    episode = episode,
                    characterList = characterList
                )
            } else {
                Landscape(
                    navController = navController,
                    date = date,
                    episode = episode,
                    characterList = characterList
                )
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
fun Portrait(
    navController: NavController,
    date: String,
    episode: EpisodeTmdb,
    characterList: List<Character>
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(top = 10.dp), elevation = 8.dp) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500" + episode.still_path,
                contentDescription = "Imagem do episódio",
                contentScale = ContentScale.Crop
            )
        }

        episode.name?.let { it1 ->
            Text(
                text = it1,
                Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }

        Text(text = "Temporada: ${episode.seasonNumber}")
        Text(text = "Episódio: ${episode.episodeNumber}")
        Text(text = "Lançamento: $date")

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
            GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(characterList.size) { index ->

                CharacterItem(
                    characterData = (characterList[index]),
                    onCharacterClicked = {
                        navController.navigate("CharacterDetailScreen/${it}") {
                            launchSingleTop = true
                        }
                    })
            }
        }
    }


}

@Composable
fun Landscape(
    navController: NavController,
    date: String,
    episode: EpisodeTmdb,
    characterList: List<Character>
) {

    Row() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(top = 10.dp), elevation = 8.dp) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500" + episode.still_path,
                    contentDescription = "Imagem do episódio",
                    contentScale = ContentScale.Crop
                )
            }

            episode.name?.let { it1 ->
                Text(
                    text = it1,
                    Modifier.padding(top = 10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            Text(text = "Temporada: ${episode.seasonNumber}")
            Text(text = "Episódio: ${episode.episodeNumber}")
            Text(text = "Lançamento: $date")

            episode.overview?.let { it1 ->
                Text(
                    text = it1,
                    Modifier.padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Personagens:",
                Modifier.padding(top = 10.dp),
                textAlign = TextAlign.Center
            )
            LazyVerticalGrid(
                GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(characterList.size) { index ->

                    CharacterItem(
                        characterData = (characterList[index]),
                        onCharacterClicked = {
                            navController.navigate("CharacterDetailScreen/${it}") {
                                launchSingleTop = true
                            }
                        })
                }
            }
        }
    }
}

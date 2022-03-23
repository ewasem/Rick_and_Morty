package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.models.SeasonTmdb
import br.com.ewapps.rickandmorty.ui.MainViewModel
import br.com.ewapps.rickandmorty.ui.RickAndMortyApp
import br.com.ewapps.rickandmorty.ui.theme.Color4
import br.com.ewapps.rickandmorty.ui.theme.Color5
import br.com.ewapps.rickandmorty.ui.theme.ColorBackground
import coil.compose.AsyncImage
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CharacterDetailScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val characterData = viewModel.selectedCharacter.collectAsState().value
    val seasonEpisodes = viewModel.characterEpisodes.collectAsState().value
    val currentOrientation = LocalConfiguration.current.orientation

    Scaffold(topBar = {
        DetailTopAppBar(onBackPressed = { navController.popBackStack() })
    }) {
        Box {
            if (currentOrientation == 1) {
                Portrait(
                    navController = navController,
                    characterData = characterData,
                    seasonEpisodes = seasonEpisodes
                )
            } else {
                Landscape(
                    navController = navController,
                    characterData = characterData,
                    seasonEpisodes = seasonEpisodes
                )
            }

        }
    }
}


@Composable
fun DetailTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = "Detalhe do Personagem", fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Botão voltar")
            }
        }, elevation = 5.dp
    )
}

/*
@Preview(showBackground = true)
@Composable
fun CharacterDetailScreenPreview() {
    CharacterDetailScreen(
        rememberNavController(), CharacterModel()
    )
}*/

@Composable
private fun Portrait(
    navController: NavController,
    characterData: Character,
    seasonEpisodes: MutableList<SeasonTmdb>
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        AsyncImage(
            model = characterData.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(
                id = R.drawable.error
            ),
            modifier = Modifier
                .fillMaxSize()
                .layoutId("image")
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .absoluteOffset(y = (-15).dp)
                .layoutId("cardDetails")
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .layoutId("columnCharacter"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                characterData.name.let { it1 ->
                    if (it1 != null) {
                        Text(
                            text = it1,
                            Modifier
                                .padding(top = 10.dp)
                                .layoutId("characterName"),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = "Espécie: ${characterData.species}",
                    Modifier
                        .padding(top = 10.dp)
                        .layoutId("species")
                )
                Text(text = "Gênero: ${characterData.gender}", Modifier.layoutId("gender"))
                Text(text = "Status: ${characterData.status}", Modifier.layoutId("status"))
                Text(text = "Origem: ${characterData.origin?.name}", Modifier.layoutId("origin"))
                Text(
                    text = "Localização: ${characterData.location?.name}",
                    Modifier.layoutId("location")
                )
                Text(
                    text = "Lista de episódios:",
                    Modifier
                        .padding(top = 10.dp)
                        .layoutId("episodesTitle"),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .layoutId("seasonCard")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        seasonEpisodes.forEach { seasonNumber ->
                            seasonNumber.name?.let { it1 ->
                                Text(
                                    text = it1,
                                    Modifier.layoutId("seasonText"),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .layoutId("episodeCard")

                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(5.dp)
                                ) {
                                    seasonNumber.episodes?.forEach {
                                        Text(
                                            text = "Episódio: ${it.episodeNumber} - ${it.name}",
                                            Modifier
                                                .clickable { navController.navigate("EpisodeDetailScreen/${it.episodeId}") {
                                                    launchSingleTop = true
                                                } }
                                                .layoutId("episodeText"),
                                            textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Landscape(
    navController: NavController,
    characterData: Character,
    seasonEpisodes: MutableList<SeasonTmdb>
) {

    Row() {
        Column(Modifier.weight(1f)) {
            AsyncImage(
                model = characterData.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(
                    id = R.drawable.error
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .layoutId("image")
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(ColorBackground),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                characterData.name.let { it1 ->
                    if (it1 != null) {
                        Text(
                            text = it1,
                            Modifier
                                .padding(top = 10.dp)
                                .layoutId("characterName"),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = "Espécie: ${characterData.species}",
                    Modifier
                        .padding(top = 10.dp)
                        .layoutId("species")
                )
                Text(text = "Gênero: ${characterData.gender}", Modifier.layoutId("gender"))
                Text(text = "Status: ${characterData.status}", Modifier.layoutId("status"))
                Text(
                    text = "Origem: ${characterData.origin?.name}",
                    Modifier.layoutId("origin")
                )
                Text(
                    text = "Localização: ${characterData.location?.name}",
                    Modifier.layoutId("location")
                )
                Text(
                    text = "Lista de episódios:",
                    Modifier
                        .padding(top = 10.dp)
                        .layoutId("episodesTitle"),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .layoutId("seasonCard")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        seasonEpisodes.forEach { seasonNumber ->
                            seasonNumber.name?.let { it1 ->
                                Text(
                                    text = it1,
                                    Modifier.layoutId("seasonText"),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .layoutId("episodeCard"), backgroundColor = ColorBackground


                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(5.dp)
                                ) {
                                    seasonNumber.episodes?.forEach {
                                        Text(
                                            text = "Episódio: ${it.episodeNumber} - ${it.name}",
                                            Modifier
                                                .clickable { navController.navigate("EpisodeDetailScreen/${it.episodeId}") {
                                                    launchSingleTop = true
                                                } }
                                                .layoutId("episodeText"),
                                            textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}







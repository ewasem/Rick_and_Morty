package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.models.Season
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterData: Character,
    seasonEpisodes: List<Season>
) {


    Scaffold(topBar = {
        DetailTopAppBar(onBackPressed = { navController.popBackStack() })
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CoilImage(
                imageModel = characterData.image,
                contentScale = ContentScale.Crop,
                error = ImageBitmap.imageResource(
                    id = R.drawable.error
                ),
                placeHolder = ImageBitmap.imageResource(
                    id = R.drawable.error
                ),
                modifier = Modifier.fillMaxSize()
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .absoluteOffset(y = (-15).dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    characterData.name.let { it1 ->
                        Text(
                            text = it1,
                            Modifier.padding(top = 10.dp),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Espécie: ${characterData.species}",
                        Modifier.padding(top = 10.dp)
                    )
                    Text(text = "Gênero: ${characterData.gender}")
                    Text(text = "Status: ${characterData.status}")
                    Text(text = "Origem: ${characterData.origin.name}")
                    Text(text = "Localização: ${characterData.location.name}")
                    Text(
                        text = "Lista de episódios:",
                        Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(5.dp)) {
                            seasonEpisodes.forEach {seasonNumber->
                                seasonNumber.season?.let { it1 -> Text(text = "Temporada $it1", color = Color.White, textAlign = TextAlign.Center) }
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)

                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(5.dp)) {
                                        seasonNumber.episodes.forEach {
                                            Text(
                                                text = "Episódio: ${it.episodeNumber} - ${it.episodeName}",
                                                Modifier.clickable { navController.navigate("EpisodeDetailScreen/${it.episodeId}/${seasonNumber.season}/${it.episodeNumber}") }, textAlign = TextAlign.Center)
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
        }, elevation = 8.dp
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
package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.models.CharacterModel
import br.com.ewapps.rickandmorty.models.Location
import br.com.ewapps.rickandmorty.models.Origin
import br.com.ewapps.rickandmorty.ui.*
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CharacterDetailScreen(navController: NavController, characterData: CharacterModel) {

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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var tempAnt = "01"
                            val list: MutableList<String> = mutableListOf()
                            for (i in characterData.episode.indices) {

                                val episodeNumber =
                                    characterData.episode[i].substringAfterLast("/").toInt()

                                val temp =
                                    EpisodeData.getEpisode(episodeNumber).season
                                val epi =
                                    EpisodeData.getEpisode(episodeNumber).episodes

                                println("episódios: $episodeNumber - Temporada $temp ep. $epi")
                                if (temp == tempAnt) {
                                    list.add(epi)
                                } else {
                                    if (list.isEmpty()) {
                                        tempAnt = temp
                                        list.clear()
                                        list.add(epi)
                                    } else {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Temporada $tempAnt",
                                                color = Color.White,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Card(
                                                shape = RoundedCornerShape(20.dp),
                                                backgroundColor = Color.White,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxSize(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    list.forEach {
                                                        Text(
                                                            text = "Episódio $it",
                                                            Modifier.padding(3.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        tempAnt = temp
                                        list.clear()
                                        list.add(epi)
                                    }
                                }
                                if (characterData.episode.lastIndex == i) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Temporada $tempAnt",
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Card(
                                            shape = RoundedCornerShape(20.dp),
                                            backgroundColor = Color.White,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                list.forEach {
                                                    Text(text = "Episódio $it")
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
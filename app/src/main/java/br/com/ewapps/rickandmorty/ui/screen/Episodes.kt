package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.models.SeasonTmdb
import br.com.ewapps.rickandmorty.ui.theme.Color4
import br.com.ewapps.rickandmorty.ui.theme.ColorBackground
import coil.compose.AsyncImage

@Composable
fun Episodes(navController: NavController, episodeList: List<SeasonTmdb>?) {
    Scaffold(topBar = {
        EpisodeTopAppBar(onBackPressed = { navController.popBackStack() })
    }) {

        LazyColumn() {
            if (episodeList != null) {
                items(episodeList.size) { index ->
                    SeasonItem(
                        item = episodeList[index],
                        onEpisodeClicked = { id: Int-> navController.navigate("EpisodeDetailScreen/${id}") })
                }
            }
        }
    }
}

@Composable
fun SeasonItem(item: SeasonTmdb, onEpisodeClicked: (id: Int) -> Unit = { _ -> }) {

    val poster = "https://image.tmdb.org/t/p/w500" + item.posterPath
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color4)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            Text(text = "Temporada ${item.seasonNumber}", Modifier.padding(bottom = 8.dp), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.size(width = 250.dp, height = 350.dp)) {
                AsyncImage(model = poster, contentDescription = "Poster Temporada", Modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
            }
            println("Imagem poster: ${item.posterPath}")
            item.overview?.let { Text(text = it, Modifier.padding(8.dp), color = Color.White, textAlign = TextAlign.Center) }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (i in item.episodes?.indices!!) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = 5.dp,
                        backgroundColor = ColorBackground
                    ) {
                        Row(
                            Modifier
                                .padding(4.dp)
                                .clickable {
                                    onEpisodeClicked(
                                        item.episodes[i].episodeId!!
                                    )
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Episódio ${item.episodes[i].episodeNumber} - ${item.episodes[i].name}",
                                Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }

    }
}


@Composable
fun EpisodeTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = "Lista de Episódios", fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Botão voltar")
            }
        }, elevation = 8.dp
    )
}

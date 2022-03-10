package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.models.EpisodeData

@Composable
fun Episodes(navController: NavController) {
    Scaffold(topBar = {
        EpisodeTopAppBar(onBackPressed = { navController.popBackStack() })
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

                    val episodeList = EpisodeData.getAllEpisodes()
                    val list: MutableList<String> = mutableListOf()
                    var tempAnt = "01"
                    println("$episodeList")
                    for (i in episodeList.indices) {
                        val temp = episodeList[i].season
                        val epi = episodeList[i].episodes

                        if (temp == tempAnt) {
                            list.add(epi)
                        } else {
                            if (list.isEmpty()) {
                                tempAnt = temp
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
                                    println("Temporada atual: $temp")
                                    println("Temporada Anterior: $tempAnt")
                                    println("Episódio: $list")
                                    tempAnt = temp
                                    list.clear()
                                    list.add(epi)
                                }
                            }
                        }/*
            if (episodeList.lastIndex == i) {
                if (list.isNotEmpty()) {
                    Card {
                        Column {
                            Text(text = "Temporada $temp")
                            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                                items(list) { episode ->
                                    Text(text = "Episódio $episode")
                                }
                            }
                        }
                    }
                }
            }*/
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

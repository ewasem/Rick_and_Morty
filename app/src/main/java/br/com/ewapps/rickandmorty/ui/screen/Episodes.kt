package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.ui.EpisodeData

@Composable
fun Episodes(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Episódios", fontWeight = FontWeight.SemiBold)
/*
        val episodeList = EpisodeData.getAllEpisodes()
        val list: MutableList<String> = mutableListOf()
        var tempAnt = "01"
        for (i in episodeList.indices) {
            val temp = episodeList[i].season
            val epi = episodeList[i].episodes
            if (temp == tempAnt) {
                list.add(epi)
            } else {
                Card() {
                    Column {
                        Text(text = "Temporada $temp")
                        LazyColumn(contentPadding = PaddingValues(8.dp)) {
                            items(list) { episode ->
                                Text(text = "Episódio $episode")
                            }
                        }
                    }
                }
                tempAnt = temp
                list.clear()
                list.add(epi)
            }
            if (episodeList.lastIndex == i) {
                Card() {
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

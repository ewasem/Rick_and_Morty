package br.com.ewapps.rickandmorty.ui.screen

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.models.Season
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Episodes(navController: NavController, episodeList: List<Season>?) {
    Scaffold(topBar = {
        EpisodeTopAppBar(onBackPressed = { navController.popBackStack() })
    }) {

            LazyColumn(Modifier.background(color = Color.Gray)) {
                if (episodeList != null) {
                    items(episodeList.size) { index ->
                        SeasonItem(item = episodeList[index])
                    }
                }
            }
        }
    }



@Composable
fun SeasonItem(item: Season) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Temporada ${item.season}", color = Color.White)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (i in item.episodes.indices) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color.LightGray,
                        elevation = 3.dp
                    ) {
                        Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Episódio ${item.episodes[i].episodeNumber} - ${item.episodes[i].episodeName}",  Modifier.padding(5.dp), textAlign = TextAlign.Center)
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

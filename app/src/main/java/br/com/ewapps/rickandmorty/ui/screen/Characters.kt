package br.com.ewapps.rickandmorty.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.components.ErrorUI
import br.com.ewapps.rickandmorty.components.LoadingUI
import br.com.ewapps.rickandmorty.components.SearchFeature
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.ui.MainViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Characters(
    navController: NavController,
    characters: List<Character>?,
    totalCharacters: Int?,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>,
    query: MutableStateFlow<String>
) {
    println(characters?.size)

    var total = totalCharacters ?: 0
    val searchedText = query.collectAsState().value
    var characterList = characters
    if (searchedText != "") {
        characterList = viewModel.characterSearched.collectAsState().value
        total = characterList.size
    }
    //Inicializa a posição que a tela está
    val listState = rememberLazyGridState()

    //Animação para empurrar a tela de personangens para baixo quando a
    //searchBar é acionada.
    val offsetAnimation: Dp by animateDpAsState(
        if (viewModel.showSearchBar.collectAsState().value) 70.dp else 0.dp,
        tween(
            durationMillis = 500
        )
    )

    when {
        isLoading.value -> {
            LoadingUI()
        }
        isError.value -> {
            ErrorUI()
        }
        else -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .absoluteOffset(y = offsetAnimation),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Total de personagens: $total", fontWeight = FontWeight.SemiBold)

                LazyVerticalGrid(

                    GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(8.dp),
                    state = listState
                ) {
                    if (characterList != null) {
                        items(characterList.size) { index ->
                            CharacterItem(
                                characterData = (characterList[index]),
                                onCharacterClicked = { id ->
                                    navController.navigate("CharacterDetailScreen/${id}") {
                                        launchSingleTop = true
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun CharacterItem(characterData: Character, onCharacterClicked: (id: Int) -> Unit = {}) {
    Card(shape = MaterialTheme.shapes.medium, elevation = 6.dp, modifier = Modifier
        .size(100.dp, 160.dp)
        .padding(8.dp)
        .clickable {
            onCharacterClicked(characterData.id!!)
        }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = characterData.image,
                contentDescription = "imagem do personagem",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(
                    id = R.drawable.error
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp, 100.dp)
            )

            characterData.name?.let {
                Text(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    text = it
                )
            }
        }

    }
}
/*
@Preview(showBackground = true)
@Composable

fun CharactersPreview() {
    CharacterItem(
        CharacterModel(listOf("1", "2", "3"), "Masculino", 1,R.drawable.im1, Location("terra"),"Rick Sanchez", Origin("Terra"), "Humano", "Vivo")
    )
}*/


package br.com.ewapps.rickandmorty.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.components.ErrorUI
import br.com.ewapps.rickandmorty.components.LoadingUI
import br.com.ewapps.rickandmorty.models.Character
import br.com.ewapps.rickandmorty.ui.MainViewModel
import coil.compose.AsyncImage
import com.skydoves.landscapist.coil.CoilImage
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
    isError: MutableState<Boolean>
) {
    println(characters?.size)



    var total = totalCharacters ?: 0
    //Para utilizar scroolToItem, é necessário usar coroutine
    val coroutineScope = rememberCoroutineScope()

    //Inicializa a posição que a tela está
    val listState = rememberLazyListState()

    //Pega a posição atual da tela, retorna 1 para Portrait ou 2 para Landscape
    val currentOrientation = LocalConfiguration.current.orientation

    //Pega a atual posição da primeira linha a ser exibida na tela
    val index = viewModel.getIndex(currentOrientation)

    //Pega o valor do offset, pois se está no meio de uma linha, ao mudar a tela, aparecerá na mesma posição
    val offset = viewModel.getOffset()

    //Se houve mudança de posição na tela, lança a funça para ir até a posição correta
    //E atualiza a orientação da tela na viewModel
    if (currentOrientation != viewModel.orientation.collectAsState().value) {
        coroutineScope.launch {
            listState.scrollToItem(index = index, offset)
            viewModel.updateOrientation(currentOrientation)
        }
    }

    //Atualiza a atual posição da tela na viewmodel
    if (listState.isScrollInProgress) {
        if (currentOrientation == 2) {
            viewModel.updateIndex(listState.firstVisibleItemIndex * 2)
            viewModel.updateOffset(listState.firstVisibleItemScrollOffset)
        } else {
            viewModel.updateIndex(listState.firstVisibleItemIndex)
            viewModel.updateOffset(listState.firstVisibleItemScrollOffset)
        }
    }

    when {
        isLoading.value -> {
            LoadingUI()
        }
        isError.value -> {
            ErrorUI()
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Total de personagens: $total", fontWeight = FontWeight.SemiBold)

                LazyVerticalGrid(
                    cells = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(8.dp),
                    state = listState
                ) {
                    if (characters != null) {
                        items(characters.size) { index ->
                            CharacterItem(
                                characterData = (characters[index]),
                                onCharacterClicked = { id ->
                                    navController.navigate("CharacterDetailScreen/${id}")
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
            AsyncImage(model = characterData.image,
                contentDescription = "imagem do personagem",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(
                    id = R.drawable.error
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp, 100.dp))

            characterData.name?.let { Text(fontSize = 16.sp, textAlign = TextAlign.Center, text = it) }
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
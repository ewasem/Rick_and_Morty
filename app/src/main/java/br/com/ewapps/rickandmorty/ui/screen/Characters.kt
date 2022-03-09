package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.models.CharacterModel
import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.Location
import br.com.ewapps.rickandmorty.ui.MockData
import br.com.ewapps.rickandmorty.models.Origin
import com.skydoves.landscapist.coil.CoilImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Characters(navController: NavController, characters: MutableList<CharacterModel>?, totaCharacters: Int?) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Total de personagens: $totaCharacters", fontWeight = FontWeight.SemiBold)


        LazyVerticalGrid(cells = GridCells.Adaptive(160.dp), contentPadding = PaddingValues(8.dp)) {
            if (characters != null) {
                items(characters.size) { index ->
                    CharacterItem(characterData = (characters[index]), onCharacterClicked = {
                        navController.navigate("CharacterDetailScreen/${index}")
                    })
                }
            }

        }
    }
}

@Composable
fun CharacterItem(characterData: CharacterModel, onCharacterClicked: () -> Unit = {}) {
    Card(shape = MaterialTheme.shapes.medium, elevation = 6.dp, modifier = Modifier
        .size(100.dp, 160.dp)
        .padding(8.dp)
        .clickable {
            onCharacterClicked()
        }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CoilImage(imageModel = characterData.image, contentScale = ContentScale.Crop, error = ImageBitmap.imageResource(
                id = R.drawable.error
            ), placeHolder = ImageBitmap.imageResource(
                id = R.drawable.error
            ), modifier = Modifier.clip(CircleShape).size(100.dp, 100.dp))
            Text(fontSize = 16.sp, textAlign = TextAlign.Center, text = characterData.name)
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
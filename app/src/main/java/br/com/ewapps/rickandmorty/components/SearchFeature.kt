package br.com.ewapps.rickandmorty.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.ewapps.rickandmorty.ui.MainViewModel
import br.com.ewapps.rickandmorty.ui.theme.Color1
import br.com.ewapps.rickandmorty.ui.theme.Color2
import br.com.ewapps.rickandmorty.ui.theme.ColorBackground

@Composable
fun SearchFeature(query: MutableState<String>, viewModel: MainViewModel) {

    val localFocusManager = LocalFocusManager.current

    AnimatedVisibility(
        visible = viewModel.showSearchBar.collectAsState().value,
        enter = slideInVertically(
            initialOffsetY = { -it }, animationSpec = tween(
                durationMillis = 500
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(
                durationMillis = 500
            )
        ),
        content = {
            Box(
                Modifier
                    .background(Color1)
                    .fillMaxWidth()
            ) {
                Card(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    backgroundColor = ColorBackground
                ) {
                    TextField(value = query.value, onValueChange = {
                        query.value = it
                        viewModel.query.value = it
                        viewModel.getFilteredCharacters()

                    }, modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Procurar", color = Color2)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "",
                                tint = Color2
                            )
                        },
                        trailingIcon = {
                            if (query.value != "") {
                                IconButton(onClick = { query.value = ""
                                viewModel.query.value = ""
                                viewModel.getFilteredCharacters()}) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "",
                                        tint = Color2
                                    )
                                }
                            }
                        },
                        textStyle = TextStyle(color = Color2, fontSize = 18.sp),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                /*if (query.value != "") {
                            viewModel.getSearchedCharacters(query.value)
                        }*/
                                viewModel.showSearchBarInCharacters()
                                localFocusManager.clearFocus()
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color2)
                    )
                }

            }
        })
}
package br.com.ewapps.rickandmorty.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.ewapps.rickandmorty.ui.MainViewModel
import br.com.ewapps.rickandmorty.ui.screen.BottomMenuScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TopBar(
    viewModel: MainViewModel,
    navController: NavController,
    onBackPressed: () -> Unit = {},
    topBarState: MutableState<Boolean>,

) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val title = viewModel.topBarTitle.collectAsState().value
    val openDialog = remember { mutableStateOf(false) }
    val items = listOf(
        "Status",
        "Espécie",
        "Gênero",
        "Limpar Filtro"
    )

    if (openDialog.value) {
        FilterDialog(openDialog = openDialog, viewModel = viewModel)
    }

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        if (currentRoute == BottomMenuScreen.Characters.route) {
            var menu by remember { mutableStateOf(false) }
            TopAppBar(
                title = { Text(text = title, fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = { viewModel.showSearchBarInCharacters() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Botão procurar"
                        )
                    }
                    IconButton(onClick = { menu = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar"
                        )
                    }
                    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
                        DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                            items.forEach {
                                DropdownMenuItem(onClick = {
                                    when(it) {
                                        "Status" -> {
                                            viewModel.filterType.value = "Status"
                                            openDialog.value = true
                                            menu = false
                                        }
                                        "Limpar Filtro" -> {
                                            viewModel.filterType.value = ""
                                            viewModel.filterString.value = ""
                                            viewModel.getFilteredCharacters()
                                            menu = false
                                        }
                                        else -> {
                                            menu = false
                                        }
                                    }

                                }) {
                                    Text(text = it)
                                }
                            }
                        }
                    }
                }, elevation = 0.dp
            )
        } else {
            TopAppBar(
                title = { Text(text = title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Botão voltar"
                        )
                    }
                }, elevation = 8.dp
            )
        }
    }
}

@Composable
fun FilterDialog(openDialog: MutableState<Boolean>, viewModel: MainViewModel) {
    Dialog(onDismissRequest = { openDialog.value = false }) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Selecione o Status:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Column(Modifier.padding(top = 10.dp)) {

                }
                Text(text = "Vivo",
                    Modifier
                        .padding(top = 3.dp)
                        .clickable {
                            viewModel.filterString.value = "Alive"
                            viewModel.getFilteredCharacters()
                            openDialog.value = false
                        })
                Text(text = "Morto",
                    Modifier
                        .padding(top = 3.dp)
                        .clickable {
                            viewModel.filterString.value = "Dead"
                            viewModel.getFilteredCharacters()
                            openDialog.value = false
                        })
                Text(text = "Desconhecido",
                    Modifier
                        .padding(top = 3.dp)
                        .clickable {
                            viewModel.filterString.value = "Unknown"
                            viewModel.getFilteredCharacters()
                            openDialog.value = false
                        })
    }
}}}
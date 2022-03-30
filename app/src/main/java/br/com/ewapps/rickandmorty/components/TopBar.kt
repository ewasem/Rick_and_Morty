package br.com.ewapps.rickandmorty.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.ewapps.rickandmorty.ui.MainViewModel
import br.com.ewapps.rickandmorty.ui.screen.BottomMenuScreen

@Composable
fun TopBar(
    viewModel: MainViewModel,
    navController: NavController,
    onBackPressed: () -> Unit = {},
    topBarState: MutableState<Boolean>
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val title = viewModel.topBarTitle.collectAsState().value

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
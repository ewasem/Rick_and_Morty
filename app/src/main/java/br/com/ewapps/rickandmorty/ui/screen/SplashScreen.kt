package br.com.ewapps.rickandmorty.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ewapps.rickandmorty.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import br.com.ewapps.rickandmorty.ui.theme.Color1

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        navController.popBackStack()
        navController.navigate(BottomMenuScreen.Characters.route) {
            launchSingleTop = true
        }

    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    // Imagem que aparecerá no centro da tela
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color1)) {

        Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.alpha(alpha))

        Box(
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()) {
            Image(painter = painterResource(id = R.drawable.logoew), contentDescription = null, modifier = Modifier
                .align(
                    Alignment.Center
                )
                .size(180.dp)
                .alpha(alpha))
        }

    }
}
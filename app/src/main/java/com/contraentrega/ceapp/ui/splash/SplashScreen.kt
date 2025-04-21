package com.contraentrega.ceapp.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.contraentrega.ceapp.R
import com.contraentrega.ceapp.BuildConfig

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        isVisible = true
        delay(2000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(visible = isVisible, enter = fadeIn()) {
                Image(
                    painter = painterResource(id = R.drawable.contraentrega1),
                    contentDescription = "Transition Logo",
                    modifier = Modifier.size(200.dp)
                )
            }
            /*Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ambiente actual: ${BuildConfig.FLAVOR.uppercase()}",
                color = Color.White
            )*/
        }
    }
}








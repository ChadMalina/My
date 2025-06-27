package com.example.my.presentation.Splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.my.presentation.navigation.ROUT_LOGIN
import com.example.my.R
import com.example.my.ui.theme.Blue80
import com.example.my.ui.theme.Purple40
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
) {
    // Launch navigation logic only once
    LaunchedEffect(Unit) {
        // Keep splash on screen for 5 seconds
        delay(5000L)
        navController.navigate(ROUT_LOGIN) {
            popUpTo(0)
        }
    }


    // UI: a full‑screen background with centered logo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = (Purple40))
        // or use .paint(painter = painterResource(R.drawable.splash_bg), contentScale = ContentScale.Crop)
    ) {
        Image(
            painter = painterResource(R.drawable.img), // your logo asset
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
    }
}
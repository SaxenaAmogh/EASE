package com.example.eventmanagement.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventmanagement.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    var scale by remember { mutableFloatStateOf(0.3f) } // Start at 50% scale
    val animatedScale: Float by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 1000) // 1 second animation
    )

    LaunchedEffect(Unit) {
        scale = 1f
        delay(1200)
//        navController.navigate("home") {
//            popUpTo("splash") { inclusive = true }
//        }
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF6298F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash), // Replace with your image resource
                        contentDescription = "Splash Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(animatedScale),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}
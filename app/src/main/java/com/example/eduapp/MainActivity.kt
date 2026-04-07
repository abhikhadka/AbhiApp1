@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.eduapp

import ImageDisplayScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.screen.TestDBScreen
import com.example.eduapp.ui.theme.EduAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val currentContext = applicationContext
        setContent {
            EduAppTheme {
                AppNav(currentContext)
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(1000))
        delay(1000) // Wait for 1 second
        onTimeout()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F1)), // Matching your logo background
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(300.dp).alpha(alpha.value)
        )
    }
}

@Composable
fun AppNav(currentContext: Context) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash"){
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("landing") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("landing"){ LandingScreen(navController) }
        composable("game") { GameScreen(currentContext, navController) }
        composable("setting") { SettingScreen(navController) }
        composable("score") { ScoreScreen(currentContext, navController) }
        composable("testDB") { TestDBScreen(currentContext) }
        composable("imageDisplay") {
            ImageDisplayScreen(
                context = currentContext,
                folder = "1",
                imageName = "level01_pic01_0.png"
            )
        }
    }
}

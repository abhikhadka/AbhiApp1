@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.eduapp

import ImageDisplayScreen
import android.media.MediaPlayer
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eduapp.navigation.Screen
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.screen.TestDBScreen
import com.example.eduapp.ui.theme.EduAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    // Always start muted to ensure music never plays automatically
    private var isMuted = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        isMuted.value = true
        setupMusic()

        setContent {
            EduAppTheme {
                AppNav(this, isMuted)
            }
        }
    }

    private fun setupMusic() {
        try {
            val resId = resources.getIdentifier("bg_music", "raw", packageName)
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(this, resId)
                mediaPlayer?.isLooping = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleMusic() {
        isMuted.value = !isMuted.value
        if (isMuted.value) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } else {
            if (mediaPlayer == null) {
                setupMusic()
            }
            mediaPlayer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isMuted.value) {
            mediaPlayer?.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(1000))
        delay(1000)
        onTimeout()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F1)),
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
fun AppNav(activity: MainActivity, isMuted: State<Boolean>) {
    val navController = rememberNavController()
    val context = activity.applicationContext
    
    NavHost(navController, startDestination = Screen.Splash.route){
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Landing.route){ 
            LandingScreen(
                navController = navController, 
                isMuted = isMuted.value,
                onToggleMusic = { activity.toggleMusic() }
            ) 
        }
        composable(Screen.Game.route) { GameScreen(context, navController) }
        composable(Screen.Setting.route) { 
            SettingScreen(
                navController = navController,
                isMuted = isMuted.value,
                onToggleMusic = { activity.toggleMusic() }
            ) 
        }
        composable(Screen.Score.route) { ScoreScreen(context, navController) }
        composable(Screen.TestDB.route) { TestDBScreen(context) }
        
        // Advanced Navigation: Screen with Arguments
        composable(
            route = Screen.ImageDisplay.route,
            arguments = listOf(
                navArgument("folder") { type = NavType.StringType },
                navArgument("imageName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val folder = backStackEntry.arguments?.getString("folder") ?: "1"
            val imageName = backStackEntry.arguments?.getString("imageName") ?: ""
            ImageDisplayScreen(context, folder, imageName)
        }
    }
}

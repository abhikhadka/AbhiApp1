@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.eduapp

import ImageDisplayScreen
import android.content.Context
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
    private var mediaPlayer: MediaPlayer? = null
    // Always start muted to ensure music never plays automatically
    private var isMuted = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // We do NOT load the preference here to ensure it ALWAYS starts muted
        // as per the requirement "never play ... until the user clicks"
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
                // Music will NOT start here because isMuted is true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleMusic() {
        isMuted.value = !isMuted.value
        
        // Optionally save to prefs if you want to remember for the NEXT toggle within the session
        // but since we want it to start muted every launch, we just manage the live state.
        
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
        // Stop/Pause music when app is not in focus (closed or backgrounded)
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        // Only resume if the user had manually unmuted it
        if (!isMuted.value) {
            mediaPlayer?.start()
        }
    }

    override fun onStop() {
        super.onStop()
        // Extra precaution for "closing" the app
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
fun AppNav(activity: MainActivity, isMuted: State<Boolean>) {
    val navController = rememberNavController()
    val context = activity.applicationContext
    
    NavHost(navController, startDestination = "splash"){
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("landing") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("landing"){ 
            LandingScreen(
                navController = navController, 
                isMuted = isMuted.value,
                onToggleMusic = { activity.toggleMusic() }
            ) 
        }
        composable("game") { GameScreen(context, navController) }
        composable("setting") { SettingScreen(navController) }
        composable("score") { ScoreScreen(context, navController) }
        composable("testDB") { TestDBScreen(context) }
        composable("imageDisplay") {
            ImageDisplayScreen(
                context = context,
                folder = "1",
                imageName = "level01_pic01_0.png"
            )
        }
    }
}

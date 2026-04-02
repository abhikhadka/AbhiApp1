@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.eduapp

import ImageDisplayScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.screen.TestDBScreen
import com.example.eduapp.ui.theme.EduAppTheme

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
fun AppNav(currentContext: Context) {
    //obtain navController
    val navController = rememberNavController()
    //set navHost and the routes
    NavHost(navController, startDestination = "game"){
        //define the home route (landing)
        composable("landing"){ LandingScreen(navController) }
        //define the game route
        composable("game") { GameScreen(currentContext, navController) }
        //define the setting route
        composable("setting") { SettingScreen(navController) }
        //define the score route
        composable("score") { ScoreScreen(currentContext, navController) }
        //define the testDB route
        composable("testDB") { TestDBScreen(currentContext) }
// define image display route

        composable("imageDisplay") {

            ImageDisplayScreen(

                context = currentContext,

                folder = "1",

                imageName = "level01_pic01_0.png" // Replace with your actual file name

            )

        }
    }

}
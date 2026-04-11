package com.example.eduapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Landing : Screen("landing")
    object Setting : Screen("setting")
    object Game : Screen("game")
    object Score : Screen("score")
    object TestDB : Screen("testDB")
    object ImageDisplay : Screen("imageDisplay/{folder}/{imageName}") {
        fun createRoute(folder: String, imageName: String) = "imageDisplay/$folder/$imageName"
    }
}

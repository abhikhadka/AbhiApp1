package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import helper.rememberAssetImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(currentContext: Context, navController: NavHostController, modifier: Modifier = Modifier) {

    val level = "1" //level: 1, 2, 3

    val imageName = "level01_pic01_0.png"

    val questionNumber = 1

    val assetPath = "$level/$imageName"

    val imageBitmap = rememberAssetImage(currentContext, assetPath)

    Scaffold(

        topBar = { TopAppBar(title = { Text("Game Screen") }) }

    ) {

            innerPadding ->

        Column(modifier

            .fillMaxSize()

            .padding(innerPadding)

            .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center) {



            Text(text = "Level: $level, Question Number: $questionNumber")

            Spacer(modifier = Modifier.height(20.dp))



            if (imageBitmap != null) {

                Image(

                    bitmap = imageBitmap,

                    contentDescription = "Image from assets",

                    modifier = Modifier

                        .fillMaxWidth()

                        .height(400.dp),

                    contentScale = ContentScale.Fit

                )

            } else {

                Text(text = "Image not found at: assets/$assetPath")

            }

            Button(onClick = {navController.navigate("score")})

            { Text("Go to Score") }

        }

    }

}



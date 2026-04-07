package com.example.eduapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.R

@Composable
fun LandingScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    // Matching the soft cream background of your logo
    val logoBackgroundColor = Color(0xFFF9F9F1) 
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(logoBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Your logo - now much larger
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Mini Edu Games",
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Play Button - Using the Green from your logo
            Button(
                onClick = { navController.navigate("game") },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "PLAY NOW", 
                    fontSize = 22.sp, 
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Button - Using the Blue from your logo
            OutlinedButton(
                onClick = { navController.navigate("setting") },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(64.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 2.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3))
            ) {
                Text(
                    text = "SETTINGS", 
                    fontSize = 18.sp
                )
            }
        }
    }
}

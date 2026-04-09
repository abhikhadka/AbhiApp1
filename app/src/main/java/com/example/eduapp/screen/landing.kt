package com.example.eduapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.R
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LandingScreen(
    navController: NavHostController, 
    modifier: Modifier = Modifier,
    isMuted: Boolean = true,
    onToggleMusic: () -> Unit = {}
) {
    val viewModel: AppViewModel = koinViewModel()
    var username by remember { mutableStateOf(viewModel.currentUsername.value) }
    val isNameValid = username.trim().length >= 3

    val logoBackgroundColor = Color(0xFFF9F9F1) 
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(logoBackgroundColor)
    ) {
        // Music Toggle on Top Right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 24.dp)
                .size(48.dp)
                .clickable { onToggleMusic() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.speaker_icon),
                contentDescription = "Toggle Music",
                modifier = Modifier.size(40.dp),
                alpha = if (isMuted) 0.5f else 1.0f 
            )
            
            if (isMuted) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(Color.Red)
                        .align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Your logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Mini Edu Games",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Username Input Field
            OutlinedTextField(
                value = username,
                onValueChange = { 
                    username = it
                    viewModel.currentUsername.value = it
                },
                label = { Text("Your Name") },
                placeholder = { Text("Enter your name to play") },
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Play Button - Goes to Setting Screen
            Button(
                onClick = { 
                    navController.navigate("setting") 
                },
                enabled = isNameValid,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "PLAY NOW", 
                    fontSize = 22.sp, 
                    color = if (isNameValid) Color.White else Color.DarkGray
                )
            }
        }
    }
}

package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.ui.theme.LogoBackground
import com.example.eduapp.viewmodel.AppViewModel
import helper.rememberAssetImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(currentContext: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = koinViewModel()
    
    val currentLevel = viewModel.selectedLevel.intValue
    val currentImages = viewModel.questionImages.value
    val currentIndex = viewModel.currentQuestionIndex.intValue
    val isGameOver = viewModel.isGameOver.value
    val score = viewModel.score.intValue
    val formattedTime = viewModel.getFormattedTime()

    // Answer state
    var answerText by remember { mutableStateOf("") }

    // Load current image
    val currentImageName = if (currentImages.isNotEmpty() && currentIndex < currentImages.size) {
        currentImages[currentIndex]
    } else ""
    
    val imageBitmap = if (currentImageName.isNotEmpty()) {
        rememberAssetImage(currentContext, "$currentLevel/$currentImageName")
    } else null

    Scaffold(
        containerColor = LogoBackground
    ) { innerPadding ->
        if (isGameOver) {
            // Game Over Result View
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Game Over!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Well done, ${viewModel.currentUsername.value}!", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Final Score: $score / 50", fontSize = 24.sp, fontWeight = FontWeight.Medium)
                Text(text = "Time: $formattedTime", fontSize = 18.sp, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(40.dp))
                
                Button(
                    onClick = { navController.navigate("score") },
                    modifier = Modifier.fillMaxWidth(0.8f).height(64.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text("SEE ALL STATISTICS")
                }
            }
        } else {
            // Main Game UI matching the requested layout
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .border(2.dp, Color(0xFFE0C097), RoundedCornerShape(8.dp)) // Subtle frame like in image
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Score: $score (/50)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF555555)
                        )
                        Text(
                            text = "Puzzle: ${currentIndex + 1} (/5)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF555555)
                        )
                    }
                    
                    Text(
                        text = "Duration: $formattedTime",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color(0xFF555555)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Player: ${viewModel.currentUsername.value}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = "Level: $currentLevel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                    }

                    // Puzzle Image Section
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "Math Puzzle",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            CircularProgressIndicator()
                        }
                    }

                    // Answer Section
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = answerText,
                            onValueChange = { if (it.all { char -> char.isDigit() }) answerText = it },
                            label = { Text("Enter your answer") },
                            modifier = Modifier.fillMaxWidth(0.9f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE0C097),
                                unfocusedBorderColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { 
                                viewModel.submitAnswer(currentContext, answerText)
                                answerText = "" 
                            },
                            enabled = answerText.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("SUBMIT", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

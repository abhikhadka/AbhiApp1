package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.viewmodel.AppViewModel
import helper.rememberAssetImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = koinViewModel()
    val context = LocalContext.current
    
    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    val levels = listOf(1, 2, 3)
    var selectedLevel by remember { mutableStateOf(viewModel.selectedLevel.intValue) }

    // Dynamic Image Preview logic
    val previewImageName = when(selectedLevel) {
        1 -> "level01_pic01_0.png"
        2 -> "level02_pic01_31.jpg"
        3 -> "level03_pic01_27.jpg"
        else -> ""
    }
    val imageBitmap = rememberAssetImage(context, "$selectedLevel/$previewImageName")

    val backgroundColor = Color(0xFFF9F9F1)

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Choose Your Level", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = Color(0xFF333333)
                )
            ) 
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Hello, ${viewModel.currentUsername.value}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Select a difficulty to see a preview",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Custom Dropdown UI
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentSize(Alignment.TopStart)
            ) {
                OutlinedCard(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Level $selectedLevel",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Level")
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(Color.White)
                ) {
                    levels.forEach { level ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Level $level", 
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) 
                            },
                            onClick = {
                                selectedLevel = level
                                viewModel.selectedLevel.intValue = level
                                expanded = false
                            }
                        )
                        if (level < 3) HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Dynamic Image Preview
            if (imageBitmap != null) {
                Text(
                    text = "Level Preview:", 
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Start).padding(start = 20.dp, bottom = 8.dp)
                )
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(200.dp)
                ) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Level Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Start Game Button
            Button(
                onClick = { 
                    // Initialize the game with 5 random images
                    viewModel.initializeGame(context, selectedLevel)
                    navController.navigate("game") 
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "START ADVENTURE", 
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

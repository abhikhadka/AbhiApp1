package com.example.eduapp.screen

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.eduapp.R
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.ui.theme.AppFont
import helper.rememberAssetImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController, 
    modifier: Modifier = Modifier,
    isMuted: Boolean = true,
    onToggleMusic: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    
    val viewModel: AppViewModel = koinViewModel(
        viewModelStoreOwner = activity ?: (context as ComponentActivity)
    )
    
    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    val levels = listOf(
        1 to "Discovery",
        2 to "Explorer",
        3 to "Master"
    )
    var selectedLevel by remember { mutableStateOf(viewModel.selectedLevel.intValue) }

    val levelNames = mapOf(1 to "Discovery", 2 to "Explorer", 3 to "Master")
    val currentLevelName = levelNames[selectedLevel] ?: ""

    // Dynamic Image Preview logic
    val previewImageName = when(selectedLevel) {
        1 -> "level01_pic01_0.png"
        2 -> "level02_pic01_31.jpg"
        3 -> "level03_pic01_27.jpg"
        else -> ""
    }
    val imageBitmap = rememberAssetImage(context, "$selectedLevel/$previewImageName")

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Text(
                        "Level Settings", 
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            // Music Toggle on Top Right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clickable { onToggleMusic() }
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.speaker_icon),
                    contentDescription = "Toggle Music",
                    modifier = Modifier.size(32.dp),
                    alpha = if (isMuted) 0.5f else 1.0f 
                )
                if (isMuted) {
                    Box(modifier = Modifier.width(32.dp).height(2.dp).background(Color.Red))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Hello (Username)
                Text(
                    text = "Hello, ${viewModel.currentUsername.value}!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Customize your game difficulty",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Attractive Custom Dropdown UI
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "SELECT LEVEL",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                    
                    Box {
                        Surface(
                            onClick = { expanded = !expanded },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp,
                                    color = if (expanded) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Level $selectedLevel",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = currentLevelName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown, 
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.81f) // Slightly smaller than the surface to fit inside padding
                                .background(Color.White)
                                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        ) {
                            levels.forEach { (level, name) ->
                                DropdownMenuItem(
                                    text = { 
                                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                            Text(
                                                "Level $level", 
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = if (selectedLevel == level) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                name,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.Gray
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedLevel = level
                                        viewModel.selectedLevel.intValue = level
                                        expanded = false
                                    },
                                    trailingIcon = {
                                        if (selectedLevel == level) {
                                            RadioButton(selected = true, onClick = null)
                                        }
                                    }
                                )
                                if (level < 3) HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp), thickness = 0.5.dp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Dynamic Image Preview
                if (imageBitmap != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = "Level Preview:", 
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = "Level Preview",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Start Game Button
                Button(
                    onClick = { 
                        viewModel.initializeGame(context, selectedLevel)
                        navController.navigate("game") 
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(32.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "START ADVENTURE", 
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

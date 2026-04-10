package com.example.eduapp.screen

import android.content.Context
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.ui.theme.AppFont
import helper.findActivity
import helper.rememberAssetImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(currentContext: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    val viewModel: AppViewModel = koinViewModel(
        viewModelStoreOwner = activity ?: (context as ComponentActivity)
    )
    
    val currentUsername = viewModel.currentUsername.value
    val currentLevel = viewModel.selectedLevel.intValue
    val currentImages = viewModel.questionImages.value
    val currentIndex = viewModel.currentQuestionIndex.intValue
    val isGameOver = viewModel.isGameOver.value
    val score = viewModel.score.intValue
    val formattedTime = viewModel.getFormattedTime()

    var answerText by remember { mutableStateOf("") }

    val currentImageName = if (currentImages.isNotEmpty() && currentIndex < currentImages.size) {
        currentImages[currentIndex]
    } else ""
    
    val imageBitmap = if (currentImageName.isNotEmpty()) {
        rememberAssetImage(context, "$currentLevel/$currentImageName")
    } else null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (isGameOver) {
            GameOverContent(innerPadding, currentUsername, score, formattedTime, isLandscape, navController)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                if (isLandscape) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Side: Image
                        Box(
                            modifier = Modifier.weight(1.2f).fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            PuzzleImage(imageBitmap)
                        }

                        // Right Side: Controls and Stats
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            GameStatsHeader(currentUsername, score, currentLevel, currentIndex)
                            Text(text = "Time: $formattedTime", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.height(12.dp))
                            AnswerInputArea(answerText, { if (it.all { char -> char.isDigit() }) answerText = it }) {
                                viewModel.submitAnswer(context, answerText)
                                answerText = ""
                            }
                        }
                    }
                } else {
                    // Portrait Layout
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GameStatsHeader(currentUsername, score, currentLevel, currentIndex)
                        Text(text = "Time: $formattedTime", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                        
                        Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                            PuzzleImage(imageBitmap)
                        }

                        AnswerInputArea(answerText, { if (it.all { char -> char.isDigit() }) answerText = it }) {
                            viewModel.submitAnswer(context, answerText)
                            answerText = ""
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameOverContent(
    innerPadding: PaddingValues, 
    username: String, 
    score: Int, 
    time: String, 
    isLandscape: Boolean,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Game Over!", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Well done, $username!", style = MaterialTheme.typography.titleLarge)
        
        if (isLandscape) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Score: $score / 50", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(24.dp))
                Text("Time: $time", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            Text("Score: $score / 50", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
            Text("Time: $time", style = MaterialTheme.typography.titleLarge)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("score") },
            modifier = Modifier.fillMaxWidth(if (isLandscape) 0.5f else 0.85f).height(56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("SEE LEADERBOARD", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun PuzzleImage(imageBitmap: androidx.compose.ui.graphics.ImageBitmap?) {
    if (imageBitmap != null) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Math Puzzle",
                modifier = Modifier.padding(8.dp).fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    } else {
        CircularProgressIndicator()
    }
}

@Composable
fun GameStatsHeader(username: String, score: Int, level: Int, index: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Player: $username", style = MaterialTheme.typography.labelSmall)
            Text("Score: $score", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Level: $level", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.secondary)
            Text("Puzzle: ${index + 1}/5", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun AnswerInputArea(text: String, onValueChange: (String) -> Unit, onSubmit: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text("Answer", fontFamily = AppFont) },
            modifier = Modifier.fillMaxWidth(0.9f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontFamily = AppFont)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSubmit,
            enabled = text.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.9f).height(52.dp),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text("SUBMIT")
        }
    }
}

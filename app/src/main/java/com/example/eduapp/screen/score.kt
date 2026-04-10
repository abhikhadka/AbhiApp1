package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.database.User
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.ui.theme.AppFont
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(currentContext: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = koinViewModel()
    val users by viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())

    // Helper to get top 5 for a level
    fun getTop5ForLevel(level: String): List<User> {
        return users.filter { it.level == level }
            .sortedWith(
                compareByDescending<User> { it.score }
                    .thenBy { it.duration }
            )
            .take(5)
    }

    val level1Top5 = getTop5ForLevel("1")
    val level2Top5 = getTop5ForLevel("2")
    val level3Top5 = getTop5ForLevel("3")

    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "LEADERBOARD", 
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
            ) {
                // Level 1 Section
                item { LevelHeader("Level 1 - Discovery") }
                if (level1Top5.isEmpty()) {
                    item { EmptyLevelPlaceholder() }
                } else {
                    itemsIndexed(level1Top5) { index, user ->
                        HighscoreCard(rank = index + 1, user = user)
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Level 2 Section
                item { LevelHeader("Level 2 - Explorer") }
                if (level2Top5.isEmpty()) {
                    item { EmptyLevelPlaceholder() }
                } else {
                    itemsIndexed(level2Top5) { index, user ->
                        HighscoreCard(rank = index + 1, user = user)
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Level 3 Section
                item { LevelHeader("Level 3 - Master") }
                if (level3Top5.isEmpty()) {
                    item { EmptyLevelPlaceholder() }
                } else {
                    itemsIndexed(level3Top5) { index, user ->
                        HighscoreCard(rank = index + 1, user = user)
                    }
                }
            }

            Button(
                onClick = { 
                    navController.navigate("landing") {
                        popUpTo("landing") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(64.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) { 
                Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "PLAY AGAIN", 
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                ) 
            }
        }
    }
}

@Composable
fun LevelHeader(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun EmptyLevelPlaceholder() {
    Text(
        "No scores for this level yet.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun HighscoreCard(rank: Int, user: User) {
    val medalColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = medalColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = rank.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (rank <= 3) Color.White else medalColor
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.username, 
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Time: ${user.duration}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Text(
                text = "${user.score}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.ui.theme.LogoBackground
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(currentContext: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = koinViewModel()
    val users by viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("App Statistics") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LogoBackground)
            ) 
        },
        containerColor = LogoBackground
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Recent Players", style = MaterialTheme.typography.titleLarge)
            
            // List users from database
            users.forEach { user ->
                Card(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                ) {
                    Text(
                        text = user.username, 
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                onClick = { navController.navigate("landing") },
                modifier = Modifier.padding(top = 24.dp)
            ) { 
                Text("Back to Start") 
            }
        }
    }
}

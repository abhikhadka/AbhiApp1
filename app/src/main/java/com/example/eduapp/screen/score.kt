package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.viewmodel.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(currentContext: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    //steps to work with DB
    val db = Room.databaseBuilder(
        currentContext,
        AppDatabase::class.java,
        "app_db"
    ).build()
    val factory = AppViewModelFactory(db.appDao())
    val viewModel: AppViewModel = viewModel(factory = factory)
    val users by viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Score Screen") }) }
    ) {
            innerPadding ->
        Column(modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)) {
            Button(onClick = {navController.navigate("landing")})
            { Text("Go to Landing") }
        }
    }
}
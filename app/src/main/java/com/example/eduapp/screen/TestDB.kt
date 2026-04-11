package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.repository.UserRepository
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.viewmodel.AppViewModelFactory
import com.example.eduapp.ui.theme.AppFont

@Composable
fun TestDBScreen(currentContext: Context, modifier: Modifier = Modifier) {
    // Steps to work with DB
    val db = Room.databaseBuilder(
        currentContext,
        AppDatabase::class.java,
        "app_db"
    )
    .fallbackToDestructiveMigration() // Prevents crash on schema changes
    .build()
    
    // Updated to use UserRepository as per the new Clean Architecture requirements
    val repository = UserRepository(db.appDao())
    val factory = AppViewModelFactory(repository)
    val viewModel: AppViewModel = viewModel(factory = factory)
    val users by viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())

    var name by remember { mutableStateOf("") }
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Username", fontFamily = AppFont) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontFamily = AppFont)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                viewModel.addUser(name)
                name = ""
            }) {
                Text("Add User", fontFamily = AppFont)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.clearUsers()
            }) {
                Text("Clear", fontFamily = AppFont)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(users) { user ->
                Text(
                    text = "ID: ${user.id}, ${user.username}, score=${user.score}, level=${user.level}",
                    fontFamily = AppFont
                )
            }
        }
    }

}

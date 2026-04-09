package com.example.eduapp.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.R
import com.example.eduapp.database.AppDao
import com.example.eduapp.database.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppViewModel(private val dao: AppDao) : ViewModel() {

    val users: Flow<List<User>> = dao.getAllUsers()
    
    // Track current user's info
    var currentUsername = mutableStateOf("")
    var selectedLevel = mutableIntStateOf(1)
    
    // Game state
    var questionImages = mutableStateOf<List<String>>(emptyList())
    var currentQuestionIndex = mutableIntStateOf(0)
    var score = mutableIntStateOf(0)
    var isGameOver = mutableStateOf(false)
    
    // Timer state
    var startTime = mutableLongStateOf(0L)
    var currentTime = mutableLongStateOf(0L)

    fun initializeGame(context: Context, level: Int) {
        // Load images synchronously to avoid the "loading" flicker on screen entry
        try {
            val allAssets = context.assets.list(level.toString()) ?: emptyArray()
            val images = allAssets.filter { file ->
                val lower = file.lowercase()
                lower.endsWith(".png") || lower.endsWith(".jpg") || 
                lower.endsWith(".jpeg") || lower.endsWith(".webp")
            }.shuffled().take(5)
            
            questionImages.value = images
        } catch (e: Exception) {
            e.printStackTrace()
            questionImages.value = emptyList()
        }

        currentQuestionIndex.intValue = 0
        score.intValue = 0
        isGameOver.value = false
        
        // Start timer
        startTime.longValue = System.currentTimeMillis()
        currentTime.longValue = 0L
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (!isGameOver.value) {
                currentTime.longValue = System.currentTimeMillis() - startTime.longValue
                delay(1000)
            }
        }
    }

    fun getFormattedTime(): String {
        val seconds = (currentTime.longValue / 1000) % 60
        val minutes = (currentTime.longValue / (1000 * 60)) % 60
        val hours = (currentTime.longValue / (1000 * 60 * 60)) % 24
        return String.format("%01d:%02d:%02d", hours, minutes, seconds)
    }

    fun submitAnswer(context: Context, answerText: String): Boolean {
        if (isGameOver.value) return false

        val currentImage = questionImages.value[currentQuestionIndex.intValue]
        // Extract answer from filename (e.g., level01_pic03_15.png -> 15)
        val correctAnswer = currentImage.substringAfterLast("_").substringBeforeLast(".").toIntOrNull() ?: 0
        val userAnswer = answerText.toIntOrNull() ?: -1

        val isCorrect = userAnswer == correctAnswer
        
        // Play sound effect
        playSound(context, if (isCorrect) R.raw.correct else R.raw.incorrect)

        if (isCorrect) {
            score.intValue += 10 // Scoring 10 points per correct answer
        }

        if (currentQuestionIndex.intValue < questionImages.value.size - 1) {
            currentQuestionIndex.intValue += 1
        } else {
            isGameOver.value = true
            // Save the user and their score to DB when game ends
            saveUserToDb()
        }
        return isCorrect
    }

    private fun playSound(context: Context, soundRes: Int) {
        try {
            MediaPlayer.create(context, soundRes).apply {
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveUserToDb() {
        viewModelScope.launch {
            // Correctly populate the User fields for the score card
            val user = User(
                username = currentUsername.value,
                level = selectedLevel.intValue.toString(),
                score = score.intValue,
                duration = (currentTime.longValue / 1000).toInt(),
                date = System.currentTimeMillis()
            )
            dao.insert(user)
        }
    }

    fun addUser(username: String) {
        viewModelScope.launch {
            val user = User(username = username)
            dao.insert(user)
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}

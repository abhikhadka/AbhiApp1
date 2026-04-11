package com.example.eduapp.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.eduapp.R
import com.example.eduapp.database.LevelStats
import com.example.eduapp.database.SyncWorker
import com.example.eduapp.database.User
import com.example.eduapp.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppViewModel(private val repository: UserRepository) : ViewModel() {

    val users: Flow<List<User>> = repository.allUsers
    
    // Advanced feature: Level statistics calculated via Room Query (now via Repository)
    val levelStats: Flow<List<LevelStats>> = repository.levelStats
    
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
        val correctAnswer = currentImage.substringAfterLast("_").substringBeforeLast(".").toIntOrNull() ?: 0
        val userAnswer = answerText.toIntOrNull() ?: -1

        val isCorrect = userAnswer == correctAnswer
        playSound(context, if (isCorrect) R.raw.correct else R.raw.incorrect)

        if (isCorrect) {
            score.intValue += 10
        }

        if (currentQuestionIndex.intValue < questionImages.value.size - 1) {
            currentQuestionIndex.intValue += 1
        } else {
            isGameOver.value = true
            saveUserToDb(context)
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

    private fun saveUserToDb(context: Context) {
        viewModelScope.launch {
            val user = User(
                username = currentUsername.value,
                level = selectedLevel.intValue.toString(),
                score = score.intValue,
                duration = (currentTime.longValue / 1000).toInt(),
                date = System.currentTimeMillis(),
                isSynced = false
            )
            repository.insertUser(user)
            
            // Trigger background sync to Cloud (Firebase)
            scheduleSync(context)
        }
    }

    private fun scheduleSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "firebase_sync",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )
    }

    fun addUser(username: String) {
        viewModelScope.launch {
            val user = User(username = username)
            repository.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            repository.clearAllUsers()
        }
    }
}

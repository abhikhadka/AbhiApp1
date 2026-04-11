package com.example.eduapp.database

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_db"
        )
        .fallbackToDestructiveMigration() // Prevent crash on schema changes
        .build()
        
        val dao = database.appDao()
        val firestore = FirebaseFirestore.getInstance()

        return try {
            val unsyncedUsers = dao.getUnsyncedUsers()
            
            unsyncedUsers.forEach { user ->
                // Upload to Firebase
                val userMap = hashMapOf(
                    "username" to user.username,
                    "level" to user.level,
                    "score" to user.score,
                    "duration" to user.duration,
                    "date" to user.date
                )
                
                firestore.collection("scores")
                    .document(user.id.toString())
                    .set(userMap)
                    .await()
                
                // Mark as synced locally
                dao.update(user.copy(isSynced = true))
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

package com.example.eduapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val level: String = "1",
    val score: Int = 0,
    val duration: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false // Track if this record is synced to cloud
)

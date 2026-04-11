package com.example.eduapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    // Advanced Query: Get statistics per level
    @Query("SELECT level, AVG(score) as avgScore, MAX(score) as highScore, COUNT(*) as totalGames FROM users GROUP BY level")
    fun getLevelStatistics(): Flow<List<LevelStats>>

    // For SyncWorker: Get unsynced users
    @Query("SELECT * FROM users WHERE isSynced = 0")
    suspend fun getUnsyncedUsers(): List<User>

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}

data class LevelStats(
    val level: String,
    val avgScore: Double,
    val highScore: Int,
    val totalGames: Int
)

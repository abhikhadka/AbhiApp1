package com.example.eduapp.repository

import com.example.eduapp.database.AppDao
import com.example.eduapp.database.LevelStats
import com.example.eduapp.database.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: AppDao) {
    
    val allUsers: Flow<List<User>> = dao.getAllUsers()
    val levelStats: Flow<List<LevelStats>> = dao.getLevelStatistics()
    
    suspend fun insertUser(user: User) {
        dao.insert(user)
    }
    
    suspend fun deleteUser(user: User) {
        dao.delete(user)
    }
    
    suspend fun clearAllUsers() {
        dao.deleteAll()
    }
    
    suspend fun updateSyncStatus(user: User) {
        dao.update(user)
    }
    
    suspend fun getUnsyncedUsers(): List<User> {
        return dao.getUnsyncedUsers()
    }
}

package com.example.pastillero.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert
    suspend fun insertUser(user: User)
    
    @Query("UPDATE usuarios SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)
}

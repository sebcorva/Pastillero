package com.example.pastillero.data.repository

import com.example.pastillero.data.User
import com.example.pastillero.data.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updatePassword(email: String, newPassword: String) {
        userDao.updatePassword(email, newPassword)
    }
}

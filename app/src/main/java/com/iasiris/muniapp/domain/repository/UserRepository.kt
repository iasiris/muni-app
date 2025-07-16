package com.iasiris.muniapp.domain.repository

import com.iasiris.muniapp.domain.model.User

interface UserRepository {
    suspend fun loginUser(email: String, password: String): String?
    suspend fun insertUser(user: User)
    suspend fun getUserIdByEmail(email: String): String
    suspend fun getUserByUserId(userId: String): User?
    suspend fun updateUser(user: User)
    suspend fun isEmailAvailable(email: String): Boolean
}
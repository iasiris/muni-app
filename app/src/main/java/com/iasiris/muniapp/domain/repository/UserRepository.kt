package com.iasiris.muniapp.domain.repository

import coil3.Uri
import com.iasiris.muniapp.domain.model.User

interface UserRepository {
    suspend fun loginUser(email: String, password: String): String?
    suspend fun insertUser(user: User): Boolean
    suspend fun getUserIdByEmail(email: String): Int
    suspend fun getUserByUserId(userId: String): User?
    suspend fun updateUser(user: User): User?
    suspend fun updateUserImage(userId: String, imageUri: Uri): String?
    suspend fun isEmailAvailable(email: String): Boolean
}
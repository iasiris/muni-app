package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.dto.UserDto

interface UserRemoteDataSource {
    suspend fun loginUser(email: String, password: String): String?
    suspend fun insertUser(user: UserDto): String?
    suspend fun getUserById(email: String): UserDto?
    suspend fun getUserIdByEmail(email: String): String?
    suspend fun updateUser(user: UserDto)
    suspend fun isEmailAvailable(email: String): Boolean
}
package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.dto.UserDto
import com.iasiris.muniapp.domain.model.User

interface UserRemoteDataSource {
    suspend fun loginUser(email: String, password: String): String?
    suspend fun getUserById(email: String): UserDto?
    suspend fun saveUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
}
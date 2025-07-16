package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.UserApiService
import com.iasiris.muniapp.data.remote.dto.UserDto
import jakarta.inject.Inject
import retrofit2.http.Path

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRemoteDataSource {

    override suspend fun loginUser(email: String, password: String): String? =
        userApiService.loginUser(LoginRequest(email, password)).id

    override suspend fun insertUser(user: UserDto) {
        userApiService.registerUser(user)
    }

    override suspend fun getUserById(userId: String): UserDto? = userApiService.getUserById(userId)

    override suspend fun getUserIdByEmail(email: String): Int =
        userApiService.getUserIdBtEmail(email)

    override suspend fun updateUser(userDto: UserDto) {
        userApiService.updateUser(userDto)
    }

    override suspend fun isEmailAvailable(email: String): Boolean =
        userApiService.getEmail(email).isNullOrEmpty()
}

data class LoginRequest(val email: String, val password: String)
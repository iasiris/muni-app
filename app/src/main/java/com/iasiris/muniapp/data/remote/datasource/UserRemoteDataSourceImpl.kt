package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.UserApiService
import com.iasiris.muniapp.data.remote.dto.UserDto
import com.iasiris.muniapp.domain.model.User
import jakarta.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRemoteDataSource {

    override suspend fun loginUser(email: String, password: String): String? =
        userApiService.loginUser(email, password).id


    override suspend fun getUserById(userId: String): UserDto? = userApiService.getUserById(userId)

    override suspend fun saveUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User): Boolean {
        TODO("Not yet implemented")
    }
}
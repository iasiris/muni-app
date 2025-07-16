package com.iasiris.muniapp.data.repository

import coil3.Uri
import com.iasiris.muniapp.data.remote.datasource.UserRemoteDataSource
import com.iasiris.muniapp.domain.mapper.userDtoToDomain
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.repository.UserRepository
import jakarta.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteDataSource,
) : UserRepository {

    override suspend fun loginUser(email: String, password: String): String? {
        return remote.loginUser(email, password)
    }


    override suspend fun insertUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserIdByEmail(email: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUserId(userId: String): User? =
        remote.getUserById(userId)?.userDtoToDomain()

    override suspend fun updateUser(user: User): User? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserImage(
        userId: String,
        imageUri: Uri
    ): String? {
        TODO("Not yet implemented")
    }


    override suspend fun isEmailAvailable(email: String): Boolean {
        TODO("Not yet implemented")
    }


}
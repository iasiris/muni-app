package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.remote.datasource.UserRemoteDataSource
import com.iasiris.muniapp.domain.mapper.userDtoToDomain
import com.iasiris.muniapp.domain.mapper.userToDto
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.repository.UserRepository
import jakarta.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteDataSource,
) : UserRepository {

    override suspend fun loginUser(email: String, password: String): String? {
        return remote.loginUser(email, password)
    }

    override suspend fun insertUser(user: User) {
        remote.insertUser(user.userToDto())
    }

    override suspend fun getUserIdByEmail(email: String): String =
        remote.getUserIdByEmail(email).toString()

    override suspend fun getUserByUserId(userId: String): User? =
        remote.getUserById(userId)?.userDtoToDomain()

    override suspend fun updateUser(user: User) {
        remote.updateUser(user.userToDto())
    }

    override suspend fun isEmailAvailable(email: String): Boolean = remote.isEmailAvailable(email)


}
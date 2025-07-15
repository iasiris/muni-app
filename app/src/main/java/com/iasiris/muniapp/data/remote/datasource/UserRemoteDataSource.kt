package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.domain.model.User
import retrofit2.Response

//TODO Usar UserDto PARA LLAMADAS A API
interface UserRemoteDataSource {
    fun getCurrentUser(): User?
    fun getUserByEmail(email:String): User?
    suspend fun saveUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
}
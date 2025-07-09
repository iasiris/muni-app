package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.domain.model.User

interface UserDataSource {
    fun getCurrentUser(): User?
    fun getUserByEmail(email:String): User?
    suspend fun saveUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
}
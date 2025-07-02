package com.iasiris.muniapp.data.local

import com.iasiris.muniapp.data.model.User

interface UserDataSource {
    fun getCurrentUser(): User?
    fun getUserByEmail(email:String): User?
    fun saveUser(user: User): Boolean
    fun updateUser(user: User): Boolean
}
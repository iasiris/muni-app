package com.iasiris.muniapp.data.local

import com.iasiris.muniapp.data.model.User
import jakarta.inject.Inject

class UserDataSourceImpl @Inject constructor() : UserDataSource {
    override fun getCurrentUser(): User? {
        return user
    }

    override fun getUserByEmail(email: String): User? {
        return user
        //return database.queryUserByEmail(email)
    }

    override fun saveUser(user: User): Boolean { //TODO("Not yet implemented")
        return true
    }

    override fun updateUser(user: User): Boolean {
        return true
    }

    private val users = mutableListOf<User>()

    private val user = User(
        "a@a.com",
        "12345678",
        "John Doe",
        "", //https://res.cloudinary.com/dudjlugll/image/upload/v1751749310/m0hbkxkcawzgfydhs3jw.jpg <- TODO for testing, delete this
        "Argentina"
    )
}
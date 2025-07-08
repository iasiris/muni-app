package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.model.User
import jakarta.inject.Inject

class UserDataSourceImpl @Inject constructor() : UserDataSource {
    //TODO implement this class to interact with the API

    override fun getCurrentUser(): User? {
        return user
    }

    override fun getUserByEmail(email: String): User? {
        return user
        //return API.queryUserByEmail(email)
    }

    override fun saveUser(user: User): Boolean { //TODO("Not yet implemented")
        return true
    }

    override fun updateUser(user: User): Boolean { //TODO("Not yet implemented")
        return true
    }

    private val user = User(
        "a@a.com",
        "12345678",
        "John Doe",
        "", //https://res.cloudinary.com/dudjlugll/image/upload/v1751749310/m0hbkxkcawzgfydhs3jw.jpg <- TODO for testing, delete this
        "Argentina"
    )
}
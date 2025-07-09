package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.domain.model.User
import jakarta.inject.Inject
//TODO SOLO HACER LLAMADO A API, NO GUARDAR NADA EN BASE DE DATOS,
//TODO guardar usuario en shared preferences o similar
class UserDataSourceImpl @Inject constructor() : UserDataSource {
    override fun getCurrentUser(): User? {
        return user
    }

    override fun getUserByEmail(email: String): User? {
        return user
        //return API.queryUserByEmail(email)
    }

    override suspend fun saveUser(user: User): Boolean { //TODO("Not yet implemented")
        return true
    }

    override suspend fun updateUser(user: User): Boolean { //TODO("Not yet implemented")
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
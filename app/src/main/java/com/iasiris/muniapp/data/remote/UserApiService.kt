package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    //TODO implement the methods for user operations
    @POST("users/login")
    suspend fun loginUser(@Body email: String, @Body password: String): UserDto

    @GET("users/{id}")//todo check if this is correct
    suspend fun getUserById(userId:String): UserDto
}
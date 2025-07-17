package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.datasource.LoginRequest
import com.iasiris.muniapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): IdResponse

    @POST("users/register")
    suspend fun registerUser(@Body userDto: UserDto): IdResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): UserDto

    @GET("users/id/{email}")
    suspend fun getUserIdByEmail(email: String): IdResponse

    @PUT("users/{id}")//todo check if this is correct
    suspend fun updateUser(@Body userDto: UserDto)

    @GET("users/email/{email}")
    suspend fun getEmail(email: String): EmailResponse
}

data class IdResponse(val id: String)

data class EmailResponse(val email: String)
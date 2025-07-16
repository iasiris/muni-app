package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String,
    val email: String,
    @SerializedName("encryptedPassword")
    val password: String,
    val fullName: String,
    val userImageUrl: String = "",
    val nationality: String = "",
)
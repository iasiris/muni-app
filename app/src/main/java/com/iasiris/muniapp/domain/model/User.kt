package com.iasiris.muniapp.domain.model

data class User(
    val id: String,
    val email: String,
    val password: String,
    val fullName: String,
    val userImageUrl: String = "",
    val nationality: String = "",
)
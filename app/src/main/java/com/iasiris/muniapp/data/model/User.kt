package com.iasiris.muniapp.data.model

data class User(
    val email: String,
    val password: String,
    val fullName: String,
    val userImageUrl: String = "https://cdn-icons-png.flaticon.com/512/9385/9385289.png",
    val nationality: String = "Argentina",
)
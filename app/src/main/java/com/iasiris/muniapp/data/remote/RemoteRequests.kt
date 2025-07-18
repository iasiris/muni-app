package com.iasiris.muniapp.data.remote

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(val email: String, val fullName: String, val password: String)
package com.iasiris.muniapp.utils

import android.util.Patterns

class CommonUtils {
    companion object {
        fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun isPasswordValid(password: String) = password.length >= 8

        fun isFullNameValid(fullName: String) = fullName.length >= 8
    }
}
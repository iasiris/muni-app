package com.iasiris.muniapp.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonUtils {
    companion object {
        fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun isPasswordValid(password: String) = password.length >= 8

        fun isNewPasswordValid(password: String, newPassword: String): Boolean =
            newPassword.length >= 8 && password != newPassword

        fun returnDate(): String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date(System.currentTimeMillis()))

    }
}
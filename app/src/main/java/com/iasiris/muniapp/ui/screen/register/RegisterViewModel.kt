package com.iasiris.muniapp.ui.screen.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.UserDataSource
import com.iasiris.muniapp.data.model.User
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
import com.iasiris.muniapp.utils.CommonUtils.Companion.isFullNameValid
import com.iasiris.muniapp.utils.CommonUtils.Companion.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    fun onEmailChange(email: String) {
        //TODO checkear si el email ya existe, hacer con repository
        _registerUiState.update { state ->
            state.copy(email = email)
        }
        verifyRegister()
    }

    fun onFullNameChange(name: String) {
        _registerUiState.update { state ->
            state.copy(fullName = name)
        }
    }

    fun onPasswordChange(password: String) {
        _registerUiState.update { state ->
            state.copy(password = password)
        }
        verifyRegister()
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _registerUiState.update { state ->
            state.copy(confirmPassword = confirmPassword)
        }
        verifyRegister()
    }

    private fun verifyRegister() {
        val email = _registerUiState.value.email
        val password = _registerUiState.value.password
        val passwordConfirm = _registerUiState.value.confirmPassword

        val isEmailValid = isEmailValid(email)
        val isPasswordValid = isPasswordValid(password)
        val isPasswordConfirmValid = isPasswordValid(passwordConfirm)
        val doPasswordsMatch = password == passwordConfirm

        val isRegisterEnabled =
            isEmailValid && isPasswordValid && isPasswordConfirmValid && doPasswordsMatch && isFullNameValid(
                _registerUiState.value.fullName
            )

        _registerUiState.update { state ->
            state.copy(
                emailError = if (!isEmailValid && email.isNotEmpty()) "Email inválido" else null,
                passwordError = if (!isPasswordValid && password.isNotEmpty()) "Contraseña tiene que tener al menos 8 caracteres" else null,
                passwordConfirmError = when {
                    passwordConfirm.isEmpty() -> null
                    !isPasswordConfirmValid -> "Contraseña debe tener al menos 8 caracteres"
                    !doPasswordsMatch -> "Las contraseñas no coinciden"
                    else -> null
                },
                isRegisterEnabled = isRegisterEnabled
            )
        }
    }

    fun onRegister(onResult: (Boolean) -> Unit) {
        val canRegister = _registerUiState.value.isRegisterEnabled
        if (canRegister) {
            val newUser = User(
                email = _registerUiState.value.email,
                fullName = _registerUiState.value.fullName,
                password = _registerUiState.value.password,
                userImageUrl = ""
            )
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    userDataSource.saveUser(newUser)
                }
                clearRegistrationForm()
                onResult(true)
            }
        } else {
            Log.d("RegisterViewModel", "Registration not enabled: $canRegister")
            onResult(false)
        }
    }

    private fun clearRegistrationForm() {
        _registerUiState.update { state ->
            state.copy(
                email = "",
                password = "",
                fullName = "",
                confirmPassword = "",
                isRegisterEnabled = false,
                isSheetVisible = false
            )
        }
    }

    fun onPasswordIconClick() {
        _registerUiState.update { state ->
            state.copy(passwordHidden = !state.passwordHidden)
        }
    }

    fun onConfirmPasswordIconClick() {
        _registerUiState.update { state ->
            state.copy(passwordConfirmHidden = !state.passwordConfirmHidden)
        }
    }
}

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val confirmPassword: String = "",
    val isRegisterEnabled: Boolean = false,
    val isSheetVisible: Boolean = false,
    val passwordHidden: Boolean = true,
    val passwordConfirmHidden: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null
)
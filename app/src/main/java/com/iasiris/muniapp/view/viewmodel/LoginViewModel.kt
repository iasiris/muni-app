package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.usecase.user.LoginUserUseCase
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
import com.iasiris.muniapp.utils.CommonUtils.Companion.isPasswordValid
import com.iasiris.muniapp.view.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun onEmailChange(email: String) {
        _loginUiState.update { state ->
            state.copy(email = email)
        }
        verifyLogin()
    }

    fun onPasswordChange(password: String) {
        _loginUiState.update { state ->
            state.copy(password = password)
        }
        verifyLogin()
    }

    private fun verifyLogin() {
        val email = _loginUiState.value.email
        val password = _loginUiState.value.password

        val isEmailValid = isEmailValid(email)
        val isPasswordValid = isPasswordValid(password)

        _loginUiState.update { state ->
            state.copy(
                isLoginEnabled = isEmailValid && isPasswordValid,
                emailError = if (!isEmailValid && email.isNotEmpty()) "Email inválido" else null,
                passwordError = if (!isPasswordValid && password.isNotEmpty()) "Contraseña tiene que tener al menos 8 caracteres" else null
            )
        }
    }

    fun onLogin() {
        viewModelScope.launch {
            _loginUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val userId = withContext(Dispatchers.IO) {
                    loginUserUseCase.invoke(_loginUiState.value.email, _loginUiState.value.password)
                } ?: throw IllegalArgumentException("Email o contraseña incorrectos")

                userPreferences.setUserId(userId)
                _loginUiState.update {
                    it.copy(
                        email = "",
                        password = "",
                        isLoginEnabled = false,
                        passwordHidden = true,
                        emailError = null,
                        passwordError = null,
                        isValidLogin = true,
                        screenState = ScreenState.Success(userId),
                        shouldNavigateToCatalog = true
                    )
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> "Sin conexión a internet"
                    is HttpException -> "Error de servidor"
                    else -> "Ocurrió un error inesperado"
                }
                _loginUiState.update { it.copy(screenState = ScreenState.Error(errorMessage)) }
                Log.e("com.iasiris.muniapp", "Error: ${e.message}")
            } catch (e: IllegalArgumentException) {
                _loginUiState.update {
                    it.copy(
                        screenState = ScreenState.Success(
                            e.message ?: "Error de autenticación"
                        ), //reseteo del estado para que usuario modfique los datos de login
                        isValidLogin = false
                    )
                }
                Log.e("com.iasiris.muniapp", "Error de autenticación: ${e.message}")
            }
        }
    }

    fun onPasswordIconClick() {
        _loginUiState.update { state ->
            state.copy(passwordHidden = !state.passwordHidden)
        }
    }

    fun resetNavigationFlag() {
        _loginUiState.update { state ->
            state.copy(shouldNavigateToCatalog = false)
        }
    }
}

data class LoginUiState(
    val screenState: ScreenState<String> = ScreenState.Success(""),
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val passwordHidden: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isValidLogin: Boolean = true,
    val shouldNavigateToCatalog: Boolean = false
)
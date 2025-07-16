package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
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
    private val loginUserUseCase: LoginUserUseCase
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
                }
                if (userId.isNullOrEmpty()) {
                    throw IllegalArgumentException("Email o contraseña incorrectos")
                } else {
                    _loginUiState.update { state ->
                        state.copy(
                            screenState = ScreenState.Success(userId)
                        )
                    }
                    //TODO guardar el userId en SharedPreferences o similar
                }
            } catch (e: IllegalArgumentException) {//Email o contraseña incorrectos
                _loginUiState.update {
                    it.copy(
                        screenState = ScreenState.Error(e.message ?: "Error de autenticación"),
                        isValidLogin = false
                    ) }
                Log.e("com.iasiris.muniapp", "Error de autenticación: ${e.message}")
            } catch (e: IOException) {
                _loginUiState.update { it.copy(screenState = ScreenState.Error("Sin conexión a internet")) }
                Log.e("com.iasiris.muniapp", "Error de red: ${e.message}")
            } catch (e: HttpException) {
                _loginUiState.update { it.copy(screenState = ScreenState.Error("Error de servidor")) }
                Log.e("com.iasiris.muniapp", "Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _loginUiState.update { it.copy(screenState = ScreenState.Error("Ocurrió un error inesperado")) }
                Log.e("com.iasiris.muniapp", "Error inesperado: ${e.message}")
            }
        }
    }

    fun onPasswordIconClick() {
        _loginUiState.update { state ->
            state.copy(passwordHidden = !state.passwordHidden)
        }
    }
}

data class LoginUiState(
    val screenState: ScreenState<String> = ScreenState.Loading,
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val passwordHidden: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isValidLogin: Boolean = true
)
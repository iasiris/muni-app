package com.iasiris.muniapp.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.UserDataSource
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
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
class LoginViewModel @Inject constructor(
    private val userDataSource: UserDataSource
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

    fun onLogin(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDataSource.getUserByEmail(_loginUiState.value.email)
            }
            val isValid = user != null &&
                    _loginUiState.value.email == user.email &&
                    _loginUiState.value.password == user.password
            onResult(isValid)
        }
    }

    fun onPasswordIconClick() {
        _loginUiState.update { state ->
            state.copy(passwordHidden = !state.passwordHidden)
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val isRegisterSheetVisible: Boolean = false,
    val passwordHidden: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null
)
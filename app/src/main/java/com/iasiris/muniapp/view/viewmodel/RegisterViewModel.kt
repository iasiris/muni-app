package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil3.network.HttpException
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.usecase.user.AddUserUseCase
import com.iasiris.muniapp.domain.usecase.user.IsEmailAvailableUseCase
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
import com.iasiris.muniapp.utils.CommonUtils.Companion.isPasswordValid
import com.iasiris.muniapp.view.ui.navigation.Routes.PRODUCT_CATALOG
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
class RegisterViewModel @Inject constructor(
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    fun onEmailChange(email: String) {
        _registerUiState.update { state ->
            state.copy(
                email = email
            )
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
            isEmailValid && isPasswordValid && isPasswordConfirmValid && doPasswordsMatch
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

    fun onRegister(navController: NavController) {
        val canRegister = _registerUiState.value.isRegisterEnabled
        if (canRegister) {
            val newUser = User(
                id = "",
                email = _registerUiState.value.email,
                fullName = _registerUiState.value.fullName,
                password = _registerUiState.value.password
            )
            viewModelScope.launch {
                _registerUiState.update { it.copy(screenState = ScreenState.Loading) }
                try {
                    val isEmailAvailable = withContext(Dispatchers.IO) {
                        isEmailAvailableUseCase.invoke(newUser.email)
                    }

                    if (!isEmailAvailable) throw IllegalArgumentException("El email ya está en uso")

                    val userId = withContext(Dispatchers.IO) {
                        addUserUseCase.invoke(newUser)
                    } ?: throw IllegalArgumentException("Error al registrar el usuario")

                    clearRegistrationForm()
                    userPreferences.setUserId(userId)
                    navController.navigate(PRODUCT_CATALOG) {
                        popUpTo(PRODUCT_CATALOG) { inclusive = true }
                    }
                    _registerUiState.update { it.copy(screenState = ScreenState.Success("")) }
                } catch (e: Exception) {
                    val errorMessage = when (e) {
                        is IOException -> "Sin conexión a internet"
                        is HttpException -> "Error de servidor"
                        else -> "Ocurrió un error inesperado"
                    }
                    _registerUiState.update { it.copy(screenState = ScreenState.Error(errorMessage)) }
                    Log.e("com.iasiris.muniapp", "Error: ${e.message}")
                } catch (e: IllegalArgumentException) {
                    _registerUiState.update {
                        it.copy(
                            screenState = ScreenState.Success(
                                e.message ?: "Error de autenticación"
                            ),//reseteo del estado para que usuario modfique los datos de register
                            isEmailValid = false
                        )
                    }
                    Log.e("com.iasiris.muniapp", "Error de autenticación: ${e.message}")
                }
            }
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
                screenState = ScreenState.Success("user"),
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
    val screenState: ScreenState<String> = ScreenState.Success(""),
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val confirmPassword: String = "",
    val isRegisterEnabled: Boolean = false,
    val passwordHidden: Boolean = true,
    val passwordConfirmHidden: Boolean = true,
    val isEmailValid: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null
)
package com.iasiris.muniapp.view.viewmodel

import android.R
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.usecase.user.AddUserUseCase
import com.iasiris.muniapp.domain.usecase.user.GetUserIdByEmailUseCase
import com.iasiris.muniapp.domain.usecase.user.IsEmailAvailableUseCase
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
import com.iasiris.muniapp.utils.CommonUtils.Companion.isFullNameValid
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
class RegisterViewModel @Inject constructor(
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val getUserIdByEmailUserCase: GetUserIdByEmailUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    fun onEmailChange(email: String) {
        _registerUiState.update { state ->
            state.copy(
                email = email,
                isEmailValid = true//todo CHEQUEAR ESTA LOGICA, NO SE SI ESTÁ BIEN
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

    fun onRegister() {//todo check this logic
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
                    if (!isEmailAvailable) {
                        throw IllegalArgumentException("El email ya está en uso")
                    }

                    val userId = withContext(Dispatchers.IO) {
                        addUserUseCase.invoke(newUser)
                    }
                    Log.d("com.iasiris.muniapp", "Usuario registrado con ID: $userId")
                    if (userId.isNullOrEmpty()) {
                        throw IllegalArgumentException("Error al registrar el usuario")
                    }

                    clearRegistrationForm()
                    userPreferences.setUserId(userId)
                    _registerUiState.update { it.copy(screenState = ScreenState.Success("")) }

                } catch (e: IllegalArgumentException) {
                    _registerUiState.update {
                        it.copy(
                            isEmailValid = false,
                            screenState = ScreenState.Error(e.message ?: "Error de autenticación"),
                        )
                    }
                    Log.e("com.iasiris.muniapp", "Error de autenticación: ${e.message}")
                } catch (e: IOException) {
                    _registerUiState.update { it.copy(screenState = ScreenState.Error("Sin conexión a internet")) }
                    Log.e("com.iasiris.muniapp", "Error de red: ${e.message}")
                } catch (e: HttpException) {
                    _registerUiState.update { it.copy(screenState = ScreenState.Error("Error de servidor")) }
                    Log.e("com.iasiris.muniapp", "Error HTTP: ${e.message}")
                } catch (e: Exception) {
                    _registerUiState.update { it.copy(screenState = ScreenState.Error("Ocurrió un error inesperado")) }
                    Log.e("com.iasiris.muniapp", "Error inesperado: ${e.message}")
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
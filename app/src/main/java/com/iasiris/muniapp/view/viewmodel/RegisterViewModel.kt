package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.usecase.user.AddUserUseCase
import com.iasiris.muniapp.domain.usecase.user.GetUserIdByEmailUseCase
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
    private val addUserUseCase: AddUserUseCase,
    private val getUserIdByEmailUserCase: GetUserIdByEmailUseCase
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

    fun onRegister() {
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
                    val isUserSaved = withContext(Dispatchers.IO) {
                        addUserUseCase.invoke(newUser)
                    }
                    if (isUserSaved) {
                        clearRegistrationForm(newUser)
                        val userId = getUserIdByEmailUserCase.invoke(newUser.email)
                        _registerUiState.update { it.copy(screenState = ScreenState.Success(newUser)) }
                        //TODO add userId to Shared preferences
                    }
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

    private fun clearRegistrationForm(user: User) {
        _registerUiState.update { state ->
            state.copy(
                email = "",
                password = "",
                fullName = "",
                confirmPassword = "",
                isRegisterEnabled = false,
                isSheetVisible = false,
                screenState = ScreenState.Success(user),
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
    val screenState: ScreenState<User> = ScreenState.Loading,
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
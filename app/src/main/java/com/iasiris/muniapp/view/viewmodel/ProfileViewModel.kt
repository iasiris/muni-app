package com.iasiris.muniapp.view.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.UserDataSource
import com.iasiris.muniapp.data.model.User
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    fun init() {
        getUser()
    }

    fun onFieldChange(field: ProfileField, value: String) {
        _profileUiState.update { state ->
            when (field) {
                ProfileField.FullName -> state.copy(
                    user = state.user.copy(fullName = value),
                    isSaveEnabled = true
                )

                ProfileField.Nationality -> state.copy(
                    user = state.user.copy(
                        nationality = value
                    ),
                    isSaveEnabled = true
                )
            }
        }
    }


    fun onEmailChange(email: String) {
        _profileUiState.update { state ->
            state.copy(
                user = state.user.copy(email = email)
            )
        }
        verifyFieldChange("email")
    }

    fun onPasswordChange(newPassword: String) {
        _profileUiState.update { state ->
            state.copy(
                user = state.user.copy(password = newPassword),
                newPassword = newPassword
            )
        }
        verifyFieldChange("password")
    }

    fun onPasswordIconClick() {
        _profileUiState.update { state ->
            state.copy(passwordHidden = !state.passwordHidden)
        }
    }

    fun onImageSelected(uri: Uri?) {
        _profileUiState.update { state ->
            state.copy(
                user = state.user.copy(userImageUrl = uri?.toString().orEmpty()),
                isSaveEnabled = true
            )
        }
    }

    fun onSaveChanges(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isValid = withContext(Dispatchers.IO) {
                userDataSource.updateUser(_profileUiState.value.user)
            }
            onResult(isValid)
            _profileUiState.update { state ->
                state.copy(user = _profileUiState.value.user, newPassword = "")
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch { //let para ejecutar el bloque solo si el usuario no es nulo
            val user = withContext(Dispatchers.IO) {
                userDataSource.getCurrentUser()
            }
            user?.let {
                _profileUiState.update { state ->
                    state.copy(
                        user = user
                    )
                }
            }
        }
    }

    private fun verifyFieldChange(field: String) {
        if (field == "email") {
            val email = _profileUiState.value.user.email
            val isEmailValid = isEmailValid(email)
            _profileUiState.update { state ->
                state.copy(
                    isSaveEnabled = isEmailValid || email.isNotEmpty(),
                    emailError = if (!isEmailValid && email.isNotEmpty()) "Email inválido" else null
                )
            }

        } else if (field == "newPassword") {
            val newPassword = _profileUiState.value.newPassword
            val isNewPasswordValid = isNewPasswordValid(newPassword)
            _profileUiState.update { state ->
                if (isNewPasswordValid) {
                    state.copy(
                        user = state.user.copy(password = newPassword),
                        isSaveEnabled = true
                    )
                } else {
                    state.copy(
                        isSaveEnabled = false,
                        passwordError = if (newPassword.isNotEmpty()) "Contraseña tiene que tener al menos 8 caracteres" else null
                    )
                }
            }

        }
    }

    private fun isNewPasswordValid(newPassword: String): Boolean =
        newPassword.length >= 8 && _profileUiState.value.user.password != newPassword
}

data class ProfileUiState(
    val user: User = User("", "", "", "", ""),
    val newPassword: String = "",
    val isSaveEnabled: Boolean = false,
    val passwordHidden: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null
)

enum class ProfileField {
    FullName, Nationality
}
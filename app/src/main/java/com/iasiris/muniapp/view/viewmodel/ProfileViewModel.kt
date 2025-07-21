package com.iasiris.muniapp.view.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import coil3.network.HttpException
import com.cloudinary.Cloudinary
import com.iasiris.muniapp.BuildConfig
import com.iasiris.muniapp.data.local.AppDatabase
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.orderhistory.DeleteOrderHistoryUseCase
import com.iasiris.muniapp.domain.usecase.user.GetUserByIdUseCase
import com.iasiris.muniapp.domain.usecase.user.IsEmailAvailableUseCase
import com.iasiris.muniapp.domain.usecase.user.UpdateUserUseCase
import com.iasiris.muniapp.utils.CommonUtils
import com.iasiris.muniapp.view.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    myApplication: Application,
    private val cloudinary: Cloudinary,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val userPreferences: UserPreferences,
    private val deleteCartItemsUseCase: DeleteCartItemsUseCase,
    private val deleteOrderHistoryUseCase: DeleteOrderHistoryUseCase,
    private val commonUtils: CommonUtils
) : AndroidViewModel(myApplication) {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    fun init() {
        getUser()
    }

    fun enableSave() {
        _profileUiState.update { it.copy(isSaveEnabled = true) }
    }

    fun onFieldChange(field: ProfileField, value: String) {
        _profileUiState.update {
            when (field) {
                ProfileField.FullName -> it.copy(
                    user = it.user.copy(fullName = value),
                    isSaveEnabled = true
                )

                ProfileField.Nationality -> it.copy(
                    user = it.user.copy(
                        nationality = value
                    ), isSaveEnabled = true
                )
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _profileUiState.update {
            it.copy(
                user = it.user.copy(email = newEmail),
            )
        }
        verifyFieldChange("email")
    }

    fun onPasswordChange(newPassword: String) {
        _profileUiState.update {
            it.copy(
                user = it.user.copy(password = newPassword)
            )
        }
        verifyFieldChange("password")
    }

    fun onPasswordIconClick() {
        _profileUiState.update { it.copy(passwordHidden = !it.passwordHidden) }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            _profileUiState.update { it.copy(screenState = ScreenState.Loading) }
            withContext(Dispatchers.IO) {
                try {
                    val inputSteam =
                        getApplication<Application>().contentResolver.openInputStream(uri)
                    val uploadResult = cloudinary.uploader().upload(
                        inputSteam, mapOf("upload_preset" to BuildConfig.CLOUDINARY_UPLOAD_PRESET)
                    )
                    val imageUrl = uploadResult["secure_url"] as String
                    _profileUiState.update { state ->
                        state.copy(
                            user = state.user.copy(userImageUrl = imageUrl),
                            isSaveEnabled = true,
                            screenState = ScreenState.Success("")
                        )
                    }
                } catch (e: Exception) {
                    handleException(e)
                }
            }
        }
    }

    fun onSaveChanges() {
        viewModelScope.launch() {
            _profileUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val isNewEmail =
                    _profileUiState.value.user.email != _profileUiState.value.originalEmail
                if (isNewEmail) {
                    val isEmailAvailable =
                        isEmailAvailableUseCase.invoke(_profileUiState.value.user.email)
                    if (!isEmailAvailable) {
                        throw IllegalArgumentException("El email ya está en uso")
                    }
                    _profileUiState.update {
                        it.copy(
                            user = it.user.copy(email = it.user.email),
                            isSaveEnabled = true,
                            isEmailAvailable = true,
                            originalEmail = "${it.user.email}"
                        )
                    }
                }

                updateUserUseCase.invoke(_profileUiState.value.user)

                _profileUiState.update {
                    it.copy(
                        user = it.user,
                        newPassword = "",
                        isSaveEnabled = false,
                        screenState = ScreenState.Success(""),
                        showSuccessToast = true,
                    )
                }
            } catch (e: IllegalArgumentException) {
                _profileUiState.update {
                    it.copy(
                        screenState = ScreenState.Success(
                            e.message ?: "Error de guardado de cambios"
                        ),
                        isEmailAvailable = false
                    )
                }
                Log.e("com.iasiris.muniapp", "Error de autenticación: ${e.message}")
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch() {
            _profileUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                // Elimina todos los registros de las tablas
                getApplication<Application>().let { app ->
                    val db = Room.databaseBuilder(
                        app, AppDatabase::class.java, "muniapp_database"
                    ).build()
                    db.cartItemDao().deleteCartItems()
                    db.orderHistoryDao().deleteOrderHistory()
                    db.orderHistoryDao().deleteOrderItems()
                    db.close()
                    app.deleteDatabase("muniapp_database")
                    app.cacheDir.deleteRecursively()
                }

                _profileUiState.update { state ->
                    state.copy(
                        screenState = ScreenState.Success(""),
                        shouldNavigateToLogin = true
                    )
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _profileUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val userId = userPreferences.userIdFlow.first()
                    ?: throw NoSuchElementException("El usuario no esta loggeado")

                val user = getUserByIdUseCase.invoke(userId)
                    ?: throw NoSuchElementException("Usuario no encontrado")

                _profileUiState.update {
                    it.copy(
                        user = user,
                        originalEmail = user.email,
                        originalPassword = user.password,
                        screenState = ScreenState.Success("")
                    )
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun verifyFieldChange(field: String) {
        if (field == "email") {
            val email = _profileUiState.value.user.email
            val isEmailValid = commonUtils.isEmailValid(email)
            _profileUiState.update { state ->
                state.copy(
                    isSaveEnabled = isEmailValid || email.isNotEmpty(),
                    emailError = if (!isEmailValid && email.isNotEmpty()) "Email inválido" else null
                )
            }
        } else if (field == "password") {
            val password = _profileUiState.value.user.password
            val originalPassword = _profileUiState.value.originalPassword
            val isPasswordValid = commonUtils.isPasswordValid(password)
            val isPasswordChanged = password != originalPassword

            val isSaveEnabled = isPasswordValid && isPasswordChanged

            val passwordError = when {
                !isPasswordValid && isPasswordChanged -> "Contraseña tiene que tener al menos 8 caracteres"
                else -> null
            }

            _profileUiState.update {
                it.copy(
                    isSaveEnabled = isSaveEnabled,
                    passwordError = passwordError
                )
            }
        }
    }

    fun clearFlag(update: (ProfileUiState) -> ProfileUiState) {
        _profileUiState.update { update(it) }
    }

    private fun handleException(e: Exception) {
        val errorMessage = when (e) {
            is NoSuchElementException -> e.message ?: "Error de carga"
            is IOException -> "Sin conexión a internet"
            is HttpException -> "Error de servidor"
            else -> "Ocurrió un error inesperado"
        }
        _profileUiState.update { it.copy(screenState = ScreenState.Error(errorMessage)) }
        Log.e("com.iasiris.muniapp", "Error: ${e.message}")
    }
}

data class ProfileUiState(
    val screenState: ScreenState<String> = ScreenState.Loading,
    val user: User = User("", "", "", "", ""),
    val originalEmail: String = "",
    val originalPassword: String = "",
    val newPassword: String = "",
    val isSaveEnabled: Boolean = false,
    val passwordHidden: Boolean = true,
    val isEmailValid: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isEmailAvailable: Boolean = true,
    val showSuccessToast: Boolean = false,
    val shouldNavigateToLogin: Boolean = false
)

enum class ProfileField {
    FullName, Nationality
}
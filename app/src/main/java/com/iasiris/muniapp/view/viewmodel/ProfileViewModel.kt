package com.iasiris.muniapp.view.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.cloudinary.Cloudinary
import com.iasiris.muniapp.BuildConfig
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.usecase.user.GetUserByIdUseCase
import com.iasiris.muniapp.domain.usecase.user.IsEmailAvailableUseCase
import com.iasiris.muniapp.domain.usecase.user.UpdateUserUseCase
import com.iasiris.muniapp.utils.CommonUtils.Companion.isEmailValid
import com.iasiris.muniapp.utils.CommonUtils.Companion.isNewPasswordValid
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
class ProfileViewModel @Inject constructor(
    myApplication: Application,
    private val cloudinary: Cloudinary,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val userPreferences: UserPreferences
) : AndroidViewModel(myApplication) {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    fun init() {
        getUser()
    }

    fun enableSave() {//TODO this can be improved
        _profileUiState.update { state ->
            state.copy(
                isSaveEnabled = true
            )
        }
    }

    fun onFieldChange(field: ProfileField, value: String) {
        _profileUiState.update { state ->
            when (field) {
                ProfileField.FullName -> state.copy(
                    user = state.user.copy(fullName = value), isSaveEnabled = true
                )

                ProfileField.Nationality -> state.copy(
                    user = state.user.copy(
                        nationality = value
                    ), isSaveEnabled = true
                )
            }
        }
    }

    fun onEmailChange(email: String) {
        _profileUiState.update { state ->
            state.copy(
                user = state.user.copy(
                    email = email,
                    //isEmailValid = true TODO check this
                )
            )
        }
        verifyFieldChange("email")
    }

    fun onPasswordChange(newPassword: String) {
        _profileUiState.update { state ->
            state.copy(
                user = state.user.copy(password = newPassword), newPassword = newPassword
            )
        }
        verifyFieldChange("password")
    }

    fun onPasswordIconClick() {
        _profileUiState.update { state ->
            state.copy(passwordHidden = !state.passwordHidden)
        }
    }

    private suspend fun uploadImage(uri: Uri) {
        withContext(Dispatchers.IO) {
            try {
                val inputSteam = getApplication<Application>().contentResolver.openInputStream(uri)
                val uploadResult = cloudinary.uploader().upload(
                    inputSteam, mapOf("upload_preset" to BuildConfig.CLOUDINARY_UPLOAD_PRESET)
                )
                val imageUrl = uploadResult["secure_url"] as String
                _profileUiState.update { state ->
                    state.copy(
                        user = state.user.copy(userImageUrl = imageUrl), isSaveEnabled = true
                    )
                }
            } catch (e: Exception) {
                Log.e("Cloudinary", "Error uploading image: ${e.message}")
            }
        }
    }

    fun onSaveChanges(imageUri: Uri?) {
        viewModelScope.launch() {
            _profileUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                //todo CHECK IF EMAIL HAS CHANGED AND THEN CHECK IF IT IS AVAILABLE
                /*val isEmailAvailable = withContext(Dispatchers.IO) {
                    isEmailAvailableUseCase.invoke(newUser.email)
                }
                if (!isEmailAvailable) {
                    throw IllegalArgumentException("El email ya está en uso")
                }*/

                imageUri?.let { uploadImage(imageUri) }

                withContext(Dispatchers.IO) {
                    updateUserUseCase.invoke(_profileUiState.value.user)
                }

                _profileUiState.update { state ->
                    state.copy(
                        user = _profileUiState.value.user,
                        newPassword = "",
                        isSaveEnabled = false,
                        screenState = ScreenState.Success(true) //TODO check this kind of data
                    )
                }
            } catch (e: IOException) {
                _profileUiState.update { it.copy(screenState = ScreenState.Error("Sin conexión a internet")) }
                Log.e("com.iasiris.muniapp", "Error de red: ${e.message}")
            } catch (e: HttpException) {
                _profileUiState.update { it.copy(screenState = ScreenState.Error("Error de servidor")) }
                Log.e("com.iasiris.muniapp", "Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _profileUiState.update { it.copy(screenState = ScreenState.Error("Ocurrió un error inesperado")) }
                Log.e("com.iasiris.muniapp", "Error inesperado: ${e.message}")
            }
        }
    }

    fun onLogout(){
        viewModelScope.launch {
            userPreferences.clearUserId()
            _profileUiState.update { it.copy(screenState = ScreenState.Success(true)) }
        }
    }

    private fun getUser() {
        viewModelScope.launch { //let para ejecutar el bloque solo si el usuario no es nulo
            _profileUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val user = withContext(Dispatchers.IO) {
                    getUserByIdUseCase.invoke("6877c6aa588db6407f587ac6")//userPreferences.userIdFlow.first()!!)
                }
                if (user == null) {
                    throw IllegalArgumentException("Usuario no encontrado")
                }
                _profileUiState.update { state ->
                    state.copy(
                        user = user, screenState = ScreenState.Success(true)
                    )
                }

            } catch (e: IllegalArgumentException) {//Usuario no encontrado en DB
                _profileUiState.update {
                    it.copy(
                        screenState = ScreenState.Error(e.message ?: "Usuario no encontrado")
                    )
                }
                Log.e("com.iasiris.muniapp", "Usuario no encontrado: ${e.message}")
            } catch (e: IOException) {
                _profileUiState.update { it.copy(screenState = ScreenState.Error("Sin conexión a internet")) }
                Log.e("com.iasiris.muniapp", "Error de red: ${e.message}")
            } catch (e: HttpException) {
                _profileUiState.update { it.copy(screenState = ScreenState.Error("Error de servidor")) }
                Log.e("com.iasiris.muniapp", "Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _profileUiState.update { it.copy(screenState = ScreenState.Error("Ocurrió un error inesperado")) }
                Log.e("com.iasiris.muniapp", "Error inesperado: ${e.message}")
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
            val isNewPasswordValid =
                isNewPasswordValid(_profileUiState.value.user.password, newPassword)
            _profileUiState.update { state ->
                if (isNewPasswordValid) {
                    state.copy(
                        user = state.user.copy(password = newPassword), isSaveEnabled = true
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
}

data class ProfileUiState(
    val screenState: ScreenState<Boolean> = ScreenState.Loading,
    val user: User = User("", "", "", "", ""),
    val newPassword: String = "",
    val isSaveEnabled: Boolean = false,
    val passwordHidden: Boolean = true,
    val isEmailValid: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isValidLogin: Boolean = true
)

enum class ProfileField {
    FullName, Nationality
}
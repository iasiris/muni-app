package com.iasiris.muniapp.view.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.paddingExtraLarge
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.view.ui.components.BackButtonWithTitle
import com.iasiris.muniapp.view.ui.components.BodyText
import com.iasiris.muniapp.view.ui.components.CustomOutlinedTextField
import com.iasiris.muniapp.view.ui.components.CustomOutlinedTextFieldPassword
import com.iasiris.muniapp.view.ui.components.PrimaryButton
import com.iasiris.muniapp.view.ui.components.SimpleCircularProgressIndicator
import com.iasiris.muniapp.view.ui.components.SubheadText
import com.iasiris.muniapp.view.ui.components.rememberToastController
import com.iasiris.muniapp.view.ui.navigation.Routes.ORDER_HISTORY
import com.iasiris.muniapp.view.viewmodel.ProfileField
import com.iasiris.muniapp.view.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    LaunchedEffect(Unit) {
        profileViewModel.init()
    }

    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val state = profileUiState.screenState

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var showImagePickerDialog by remember { mutableStateOf(false) }
    val cameraError = stringResource(id = R.string.camera_error)

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            profileViewModel.uploadImage(it)
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            imageUri?.let { profileViewModel.uploadImage(it) }
        }
    }

    // Permission launcher
    val permissionRequired = stringResource(id = R.string.permission_required)
    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            showImagePickerDialog = true
        } else {
            Toast.makeText(context, permissionRequired, Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPermissions() {
        val permissions = mutableListOf<String>().apply {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }.toTypedArray()

        if (permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
            showImagePickerDialog = true
        } else {
            multiplePermissionLauncher.launch(permissions)
        }
    }

    // Toasts
    val toast = rememberToastController()

    LaunchedEffect(profileUiState.isEmailAvailable) {
        if (!profileUiState.isEmailAvailable) {
            toast.show(
                messageRes = R.string.invalid_email,
                onDismiss = { profileViewModel.clearFlag { it.copy(isEmailAvailable = true) } }
            )
        }
    }

    LaunchedEffect(profileUiState.showSuccessToast) {
        if (profileUiState.showSuccessToast) {
            toast.show(
                messageRes = R.string.user_saved_successfully,
                onDismiss = { profileViewModel.clearFlag { it.copy(showSuccessToast = false) } }
            )
        }
    }

    when (state) {
        is ScreenState.Loading -> {
            SimpleCircularProgressIndicator()
        }

        is ScreenState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = ScrollState(0),
                        enabled = true
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BackButtonWithTitle(
                    title = stringResource(id = R.string.profile_title),
                    onBackButtonClick = { navController.popBackStack() })

                //Profile Image
                IconButton(
                    onClick = {
                        checkPermissions()
                        profileViewModel.enableSave()
                    },
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    if (profileUiState.user.userImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = profileUiState.user.userImageUrl,
                            contentDescription = stringResource(id = R.string.product_image),
                            onError = {
                                Log.i(
                                    "AsyncImage",
                                    "Error loading image ${it.result.throwable.message}"
                                )
                            },
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        if (imageUri != null) {
                            val bitmap = remember(imageUri) {
                                val source =
                                    ImageDecoder.createSource(
                                        context.contentResolver,
                                        imageUri!!
                                    )
                                ImageDecoder.decodeBitmap(source).asImageBitmap()
                            }
                            Image(
                                bitmap = bitmap,
                                contentDescription = stringResource(id = R.string.user_icon),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.muni_icon),
                                contentDescription = stringResource(id = R.string.user_icon),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(paddingMedium))

                CustomOutlinedTextField(
                    label = stringResource(id = R.string.email_label),
                    text = profileUiState.user.email,
                    onValueChange = profileViewModel::onEmailChange,
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    errorMessage = profileUiState.emailError
                )

                CustomOutlinedTextFieldPassword(
                    label = stringResource(id = R.string.password),
                    text = profileUiState.user.password,
                    onValueChange = profileViewModel::onPasswordChange,
                    leadingIcon = Icons.Default.Password,
                    passwordHidden = profileUiState.passwordHidden,
                    onVisibilityToggle = { profileViewModel.onPasswordIconClick() },
                    errorMessage = profileUiState.passwordError
                )

                CustomOutlinedTextField(
                    label = stringResource(id = R.string.full_name_label),
                    text = profileUiState.user.fullName,
                    onValueChange = {
                        profileViewModel.onFieldChange(
                            ProfileField.FullName,
                            it
                        )
                    },
                    leadingIcon = Icons.Default.PersonOutline,
                )

                CustomOutlinedTextField(
                    label = stringResource(id = R.string.nationality_label),
                    text = profileUiState.user.nationality,
                    onValueChange = {
                        profileViewModel.onFieldChange(
                            ProfileField.Nationality,
                            it
                        )
                    },
                    leadingIcon = Icons.Default.Public
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = paddingExtraLarge, end = paddingExtraLarge),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PrimaryButton(
                        label = stringResource(id = R.string.save_changes),
                        onClick = profileViewModel::onSaveChanges,
                        enabled = profileUiState.isSaveEnabled
                    )

                    PrimaryButton(
                        label = stringResource(id = R.string.order_history),
                        onClick = { navController.navigate(ORDER_HISTORY) },
                    )
                    PrimaryButton(
                        label = stringResource(id = R.string.logout),
                        onClick = {
                            profileViewModel.onLogout()

                        }
                    )
                }

                if (showImagePickerDialog) {
                    AlertDialog(
                        onDismissRequest = { showImagePickerDialog = false },
                        confirmButton = {
                            PrimaryButton(
                                label = stringResource(id = R.string.gallery),
                                onClick = {
                                    showImagePickerDialog = false
                                    galleryLauncher.launch("image/*")
                                }
                            )
                        },
                        dismissButton = {
                            PrimaryButton(
                                label = stringResource(id = R.string.camera),
                                onClick = {
                                    showImagePickerDialog = false
                                    try {
                                        val photoFile = File.createTempFile(
                                            "profile_${System.currentTimeMillis()}",
                                            ".jpg",
                                            context.cacheDir
                                        )
                                        imageUri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            photoFile
                                        )
                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA
                                            ) == PackageManager.PERMISSION_GRANTED
                                        ) {
                                            cameraLauncher.launch(imageUri!!)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                cameraError,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Log.e(
                                            "com.iasiris.muniapp",
                                            "Error: ${e.message} - ${e.cause}"
                                        )
                                    }
                                }
                            )
                        },
                        title = { BodyText(stringResource(id = R.string.select_image)) },
                    )
                }
            }
        }

        is ScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubheadText(
                        text = state.message,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    PrimaryButton(
                        onClick = { profileViewModel.init() },
                        label = "${stringResource(id = R.string.retry)}",
                    )
                }
            }
        }

    }

}

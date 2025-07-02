package com.iasiris.muniapp.ui.screen.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.components.BackButtonWithTitle
import com.iasiris.muniapp.utils.components.CustomOutlinedTextField
import com.iasiris.muniapp.utils.components.CustomOutlinedTextFieldPassword
import com.iasiris.muniapp.utils.components.PrimaryButton
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userSaved = stringResource(id = R.string.user_changes_saved)
    val userNotSaved = stringResource(id = R.string.user_changes_not_saved)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileViewModel.onImageSelected(uri)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonWithTitle( //TODO no va para atras
                title = stringResource(id = R.string.profile_title),
                onBackButtonClick = { navController.popBackStack() }
            )

            IconButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = profileUiState.user.userImageUrl,
                    contentDescription = stringResource(id = R.string.user_icon),
                    onError = {
                        Log.i(
                            "AsyncImage",
                            "Error loading image ${it.result.throwable.message}"
                        )
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(paddingMedium))

            CustomOutlinedTextField(
                label = stringResource(id = R.string.email_label),
                text = profileUiState.user.email,
                onValueChange = profileViewModel::onEmailChange,
                leadingIcon = Icons.Default.Email,
            )

            CustomOutlinedTextFieldPassword(
                label = stringResource(id = R.string.password),
                text = profileUiState.user.password,
                onValueChange = profileViewModel::onPasswordChange,
                leadingIcon = Icons.Default.Password,
                passwordHidden = profileUiState.passwordHidden,
                onVisibilityToggle = { profileViewModel.onPasswordIconClick() },
            )

            CustomOutlinedTextField(
                label = stringResource(id = R.string.fullname_label),
                text = profileUiState.user.fullName,
                onValueChange = { profileViewModel.onFieldChange(ProfileField.FullName, it) },
                leadingIcon = Icons.Default.PersonOutline,
            )

            CustomOutlinedTextField(
                label = stringResource(id = R.string.nationality_label),
                text = profileUiState.user.nationality,
                onValueChange = { profileViewModel.onFieldChange(ProfileField.Nationality, it) },
                leadingIcon = Icons.Default.Public
            )

            Spacer(modifier = Modifier.height(paddingLarge))

            PrimaryButton( //TODO no cambia a enabled
                label = stringResource(id = R.string.save_changes),
                onClick = {
                    profileViewModel.onSaveChanges { isValid ->
                        if (isValid) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(userSaved)
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(userNotSaved)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = paddingExtraSmall),
                enabled = profileUiState.isSaveEnabled
            )
        }
    }
}
package com.iasiris.muniapp.view.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.view.ui.components.CustomTextField
import com.iasiris.muniapp.view.ui.components.CustomTextFieldPassword
import com.iasiris.muniapp.view.ui.components.PrimaryButton
import com.iasiris.muniapp.view.ui.components.SimpleCircularProgressIndicator
import com.iasiris.muniapp.view.ui.components.SubheadText
import com.iasiris.muniapp.view.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.view.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel
) {

    val registerUiState by registerViewModel.registerUiState.collectAsStateWithLifecycle()
    val state = registerUiState.screenState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()//TODO Mover a catch exception??
    val invalidEmail = stringResource(id = R.string.invalid_email)

    LaunchedEffect(registerUiState.screenState is ScreenState.Success) {
        navController.navigate(PRODUCT_CATALOG)
    }
    LaunchedEffect(registerUiState.isEmailValid) {
        if (!registerUiState.isEmailValid) {
            snackbarHostState.showSnackbar(invalidEmail)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is ScreenState.Loading -> {
                SimpleCircularProgressIndicator()
            }

            is ScreenState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    SubheadText(
                        text = stringResource(R.string.create_account),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = paddingSmall),
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        modifier = Modifier
                            .padding(horizontal = paddingLarge, vertical = paddingLarge),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(paddingLarge),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            CustomTextField(
                                value = registerUiState.email,
                                onValueChange = registerViewModel::onEmailChange,
                                label = stringResource(id = R.string.email),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                errorMessage = registerUiState.emailError
                            )

                            Spacer(modifier = Modifier.height(paddingSmall))

                            CustomTextField(
                                value = registerUiState.fullName,
                                onValueChange = registerViewModel::onFullNameChange,
                                label = stringResource(id = R.string.full_name),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            )

                            Spacer(modifier = Modifier.height(paddingSmall))

                            CustomTextFieldPassword(
                                value = registerUiState.password,
                                onValueChange = registerViewModel::onPasswordChange,
                                label = stringResource(id = R.string.password),
                                passwordHidden = registerUiState.passwordHidden,
                                onVisibilityToggle = { registerViewModel.onPasswordIconClick() },
                                errorMessage = registerUiState.passwordError
                            )

                            Spacer(modifier = Modifier.height(paddingSmall))

                            CustomTextFieldPassword(
                                value = registerUiState.confirmPassword,
                                onValueChange = registerViewModel::onConfirmPasswordChange,
                                label = stringResource(id = R.string.confirm_password),
                                passwordHidden = registerUiState.passwordConfirmHidden,
                                onVisibilityToggle = { registerViewModel.onConfirmPasswordIconClick() },
                                errorMessage = registerUiState.passwordConfirmError
                            )

                            Spacer(modifier = Modifier.height(paddingMedium))

                            PrimaryButton(
                                label = stringResource(id = R.string.sing_in),
                                onClick = {
                                    registerViewModel.onRegister()
                                },
                                enabled = registerUiState.isRegisterEnabled
                            )
                        }
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
                        //TODO deberia ir en dos lados?
                        LaunchedEffect(registerUiState.isEmailValid) {
                            if (!registerUiState.isEmailValid) {
                                snackbarHostState.showSnackbar(invalidEmail)
                            }
                        }
                        SubheadText(
                            text = state.message,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        PrimaryButton(
                            onClick = { registerViewModel.onRegister() },
                            label = "${stringResource(id = R.string.retry)}",
                        )
                    }
                }
            }
        }
    }
}
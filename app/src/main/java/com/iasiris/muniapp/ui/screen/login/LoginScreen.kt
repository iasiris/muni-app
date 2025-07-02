package com.iasiris.muniapp.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.components.CaptionText
import com.iasiris.muniapp.utils.components.CustomTextField
import com.iasiris.muniapp.utils.components.CustomTextFieldPassword
import com.iasiris.muniapp.utils.components.PrimaryButton
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val invalidLogin = stringResource(id = R.string.invalid_login)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .padding(horizontal = paddingLarge, vertical = paddingLarge),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.muni_icon),
                        contentDescription = stringResource(id = R.string.shopping_bag),
                    )

                    Spacer(modifier = Modifier.height(paddingMedium))

                    CustomTextField(
                        label = stringResource(id = R.string.email),
                        value = loginUiState.email,
                        onValueChange = loginViewModel::onEmailChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        errorMessage = loginUiState.emailError
                    )

                    Spacer(modifier = Modifier.height(paddingMedium))

                    CustomTextFieldPassword(
                        label = stringResource(id = R.string.password),
                        value = loginUiState.password,
                        onValueChange = loginViewModel::onPasswordChange,
                        passwordHidden = loginUiState.passwordHidden,
                        onVisibilityToggle = { loginViewModel.onPasswordIconClick() },
                        errorMessage = loginUiState.passwordError
                    )

                    Spacer(modifier = Modifier.height(paddingMedium))

                    Spacer(modifier = Modifier.height(paddingLarge))

                    PrimaryButton(
                        label = stringResource(id = R.string.login),
                        onClick = {
                            loginViewModel.onLogin { isValid ->
                                if (isValid) {
                                    navigateToHome()
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(invalidLogin)
                                    }
                                }
                            }
                        },
                        enabled = loginUiState.isLoginEnabled
                    )
                }
            }

            Spacer(modifier = Modifier.height(paddingLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CaptionText(
                    text = stringResource(id = R.string.do_not_have_account)
                )

                TextButton(
                    onClick = { navigateToRegister() },
                    modifier = Modifier.wrapContentSize()
                ) {
                    CaptionText(
                        text = stringResource(id = R.string.sing_up_here),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.usecase.user.LoginUserUseCase
import com.iasiris.muniapp.utils.CommonUtils
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUserUseCase: LoginUserUseCase = mockk()
    private val userPreferences: UserPreferences = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        mockkStatic("com.iasiris.muniapp.utils.CommonUtils")
        viewModel = LoginViewModel(loginUserUseCase, userPreferences)
    }

    @Test
    fun getLoginUiState() = runTest {
        val viewModel = LoginViewModel(loginUserUseCase, userPreferences)
        val state = viewModel.loginUiState.value

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertTrue(state.passwordHidden)
        assertFalse(state.shouldNavigateToCatalog)
        assertTrue(state.screenState is ScreenState.Success)
    }

    //@Test
    fun loginWithValidCredentialsUpdatesUiStateToSuccess() = runTest {
        every { CommonUtils.isEmailValid(any()) } returns true

        coEvery { loginUserUseCase.invoke("valid@example.com", "password123") } returns "userId123"
        coEvery { userPreferences.setUserId("userId123") } returns Unit

        viewModel.onEmailChange("valid@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLogin()
        advanceUntilIdle()

        val state = viewModel.loginUiState.value
        assertTrue(state.screenState is ScreenState.Success)
        assertEquals("userId123", (state.screenState as ScreenState.Success).data)
        assertTrue(state.shouldNavigateToCatalog)
    }

    @Test
    fun passwordVisibilityTogglesCorrectly() {
        val initialVisibility = viewModel.loginUiState.value.passwordHidden

        viewModel.onPasswordIconClick()
        val stateAfterFirstClick = viewModel.loginUiState.value
        assertEquals(!initialVisibility, stateAfterFirstClick.passwordHidden)

        viewModel.onPasswordIconClick()
        val stateAfterSecondClick = viewModel.loginUiState.value
        assertEquals(initialVisibility, stateAfterSecondClick.passwordHidden)
    }

}
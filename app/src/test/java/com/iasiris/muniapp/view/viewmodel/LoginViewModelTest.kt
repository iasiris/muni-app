package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.usecase.user.LoginUserUseCase
import com.iasiris.muniapp.utils.CommonUtils
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockUser
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUserUseCase: LoginUserUseCase = mockk()
    private val userPreferences: UserPreferences = mockk()
    private val commonUtils: CommonUtils = mockk(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(loginUserUseCase, userPreferences, commonUtils)
    }

    @Test
    fun getLoginUiState() = runTest {
        val state = viewModel.loginUiState.value

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertTrue(state.passwordHidden)
        assertFalse(state.shouldNavigateToCatalog)
        assertTrue(state.screenState is ScreenState.Success)
    }

    @Test
    fun loginWithValidCredentialsAndNavigatesToCatalog() = runTest {

        coEvery {
            loginUserUseCase.invoke(
                mockUser().email,
                mockUser().password
            )
        } returns mockUser().id
        coEvery { userPreferences.setUserId(any()) } returns Unit

        viewModel.onEmailChange(mockUser().email)
        viewModel.onPasswordChange(mockUser().password)
        viewModel.onLogin()
        advanceUntilIdle()

        val state = viewModel.loginUiState.value
        assertTrue(state.screenState is ScreenState.Success)
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
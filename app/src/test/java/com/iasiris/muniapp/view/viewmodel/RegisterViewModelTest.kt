package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.usecase.user.AddUserUseCase
import com.iasiris.muniapp.domain.usecase.user.IsEmailAvailableUseCase
import com.iasiris.muniapp.utils.CommonUtils
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockUser
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase = mockk()
    private val addUserUseCase: AddUserUseCase = mockk()
    private val userPreferences: UserPreferences = mockk()
    private val commonUtils: CommonUtils =
        mockk(relaxed = true)
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        viewModel =
            RegisterViewModel(isEmailAvailableUseCase, addUserUseCase, userPreferences, commonUtils)
    }

    @Test
    fun emailChangeUpdatesUiState() = runTest {
        every { commonUtils.isEmailValid(mockUser().email) } returns true
        viewModel.onEmailChange(mockUser().email)
        advanceUntilIdle()

        val state = viewModel.registerUiState.value

        assertEquals(mockUser().email, state.email)
    }

    @Test
    fun passwordChangeUpdatesUiState() = runTest {
        viewModel.onPasswordChange(mockUser().password)
        advanceUntilIdle()

        val state = viewModel.registerUiState.value

        assertEquals(mockUser().password, state.password)
    }

    @Test
    fun registerWithValidDataAndNavigatesToCatalog() = runTest {
        every { commonUtils.isEmailValid(mockUser().email) } returns true
        every { commonUtils.isPasswordValid(mockUser().password) } returns true
        coEvery { isEmailAvailableUseCase.invoke(mockUser().email) } returns true
        coEvery { userPreferences.setUserId(any()) } returns Unit
        coEvery { addUserUseCase.invoke(any()) } returns mockUser().id

        viewModel.onEmailChange(mockUser().email)
        viewModel.onPasswordChange(mockUser().password)
        viewModel.onFullNameChange(mockUser().fullName)
        viewModel.onConfirmPasswordChange(mockUser().password)
        viewModel.onRegister()
        advanceUntilIdle()

        val state = viewModel.registerUiState.value
        assertTrue(state.screenState is ScreenState.Success)
        assertTrue(state.shouldNavigateToCatalog)
    }

    @Test
    fun passwordVisibilityTogglesCorrectly() {
        val initialVisibility = viewModel.registerUiState.value.passwordHidden

        viewModel.onPasswordIconClick()
        val stateAfterFirstClick = viewModel.registerUiState.value
        assertEquals(!initialVisibility, stateAfterFirstClick.passwordHidden)

        viewModel.onPasswordIconClick()
        val stateAfterSecondClick = viewModel.registerUiState.value
        assertEquals(initialVisibility, stateAfterSecondClick.passwordHidden)
    }

}
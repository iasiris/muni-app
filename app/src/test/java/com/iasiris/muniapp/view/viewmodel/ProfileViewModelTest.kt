package com.iasiris.muniapp.view.viewmodel

import android.app.Application
import coil3.Uri
import com.cloudinary.Cloudinary
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.orderhistory.DeleteOrderHistoryUseCase
import com.iasiris.muniapp.domain.usecase.user.GetUserByIdUseCase
import com.iasiris.muniapp.domain.usecase.user.IsEmailAvailableUseCase
import com.iasiris.muniapp.domain.usecase.user.UpdateUserUseCase
import com.iasiris.muniapp.utils.CommonUtils
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockUser
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val myApplication: Application = mockk(relaxed = true)
    private val cloudinary: Cloudinary = mockk(relaxed = true)
    private val getUserByIdUseCase: GetUserByIdUseCase = mockk()
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase = mockk()
    private val updateUserUseCase: UpdateUserUseCase = mockk()
    private val userPreferences: UserPreferences = mockk()
    private val deleteCartItemsUseCase: DeleteCartItemsUseCase = mockk()
    private val deleteOrderHistoryUseCase: DeleteOrderHistoryUseCase = mockk()
    private val commonUtils: CommonUtils = mockk()

    private lateinit var viewModel: ProfileViewModel
    @Before
    fun setUp() {
        every { userPreferences.userIdFlow } returns flowOf(mockUser().id)
        viewModel = ProfileViewModel(
            myApplication,
            cloudinary,
            getUserByIdUseCase,
            isEmailAvailableUseCase,
            updateUserUseCase,
            userPreferences,
            deleteCartItemsUseCase,
            deleteOrderHistoryUseCase,
            commonUtils
        )
    }

    @Test
    fun initLoadsUserData() = runTest {
        val mockUser = mockUser()
        coEvery { getUserByIdUseCase.invoke(mockUser().id) } returns mockUser

        viewModel.init()
        advanceUntilIdle()


        val state = viewModel.profileUiState.value
        assertEquals(mockUser, state.user)
        assertEquals(mockUser.email, state.originalEmail)
        assertEquals(mockUser.password, state.originalPassword)
        assertTrue(state.screenState is ScreenState.Success)
    }

    @Test
    fun onFieldChangeUpdatesState() {
        viewModel.onFieldChange(ProfileField.FullName, mockUser().fullName)

        val state = viewModel.profileUiState.value
        assertEquals(mockUser().fullName, state.user.fullName)
        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun onEmailChangeUpdatesStateAndValidate() {
        every { commonUtils.isEmailValid(mockUser().email) } returns true

        viewModel.onEmailChange(mockUser().email)

        val state = viewModel.profileUiState.value
        assertEquals(mockUser().email, state.user.email)
        assertNull(state.emailError)
    }

    @Test
    fun onPasswordChangeUpdateStateAndValidate() {
        every { commonUtils.isPasswordValid(mockUser().password) } returns true

        viewModel.onPasswordChange(mockUser().password)

        val state = viewModel.profileUiState.value
        assertEquals(mockUser().password, state.user.password)
        assertNull(state.passwordError)
    }

    @Test
    fun onSaveChangesUpdatesUserWithNewEmail() = runTest {
        coEvery { isEmailAvailableUseCase.invoke("new@email.com") } returns true
        coEvery { updateUserUseCase.invoke(any()) } returns Unit

        viewModel.onSaveChanges()
        advanceUntilIdle()

        val state = viewModel.profileUiState.value
        assertFalse(state.isSaveEnabled)
        assertTrue(state.showSuccessToast)
        assertTrue(state.screenState is ScreenState.Success)
    }

    //@Test
    fun onLogoutCleansDataAndNavigateToLogin() = runTest {
        coEvery { deleteCartItemsUseCase.invoke() } returns Unit
        coEvery { deleteOrderHistoryUseCase.invoke() } returns Unit

        viewModel.onLogout()
        advanceUntilIdle()

        val state = viewModel.profileUiState.value
        assertTrue(state.shouldNavigateToLogin)
        assertTrue(state.screenState is ScreenState.Success)
    }
}
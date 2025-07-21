package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.remote.datasource.UserRemoteDataSource
import com.iasiris.muniapp.domain.mapper.userToDto
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockUser
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepositoryImpl
    private val remote: UserRemoteDataSource = mockk()

    @Before
    fun setup() {
        userRepository = UserRepositoryImpl(remote)
    }

    @Test
    fun loginUserWithValidCredentialsReturnsUserId() = runTest {
        coEvery { remote.loginUser(mockUser().email, mockUser().password) } returns mockUser().id

        val result = userRepository.loginUser(mockUser().email, mockUser().password)

        assertEquals(mockUser().id, result)
    }

    @Test
    fun insertUserReturnsUserId() = runTest {
        val user = mockUser()
        coEvery { remote.insertUser(any()) } returns mockUser().id

        val result = userRepository.insertUser(user)

        assertEquals(mockUser().id, result)
    }

    @Test
    fun getUserIdByEmailReturnsCorrectId() = runTest {
        coEvery { remote.getUserIdByEmail(mockUser().email) } returns mockUser().id

        val result = userRepository.getUserIdByEmail(mockUser().email)

        assertEquals(mockUser().id, result)
    }

    @Test
    fun updateUserCallsRemoteUpdateUser() = runTest {
        val user = mockUser()
        coEvery { remote.updateUser(any()) } just Runs

        userRepository.updateUser(user)

        coVerify { remote.updateUser(user.userToDto()) }
    }

    @Test
    fun isEmailAvailableReturnsTrueWhenAvailable() = runTest {
        coEvery { remote.isEmailAvailable(mockUser().email) } returns true

        val result = userRepository.isEmailAvailable(mockUser().email)

        assertTrue(result)
    }

    @Test
    fun isEmailAvailableReturnsFalseWhenNotAvailable() = runTest {
        coEvery { remote.isEmailAvailable("taken@example.com") } returns false

        val result = userRepository.isEmailAvailable("taken@example.com")

        assertFalse(result)
    }

}
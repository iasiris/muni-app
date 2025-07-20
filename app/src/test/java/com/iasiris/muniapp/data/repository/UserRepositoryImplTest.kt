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
import org.junit.Assert.assertNull
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
        coEvery { remote.loginUser("valid@example.com", "password123") } returns "userId123"

        val result = userRepository.loginUser("valid@example.com", "password123")

        assertEquals("userId123", result)
    }

    @Test
    fun loginUserWithInvalidCredentialsReturnsNull() = runTest {
        coEvery { remote.loginUser("invalid@example.com", "wrongpassword") } returns null

        val result = userRepository.loginUser("invalid@example.com", "wrongpassword")

        assertNull(result)
    }

    @Test
    fun insertUserReturnsUserId() = runTest {
        val user = mockUser()
        coEvery { remote.insertUser(any()) } returns "userId123"

        val result = userRepository.insertUser(user)

        assertEquals("userId123", result)
    }

    @Test
    fun getUserIdByEmailReturnsCorrectId() = runTest {
        coEvery { remote.getUserIdByEmail("john@example.com") } returns "userId123"

        val result = userRepository.getUserIdByEmail("john@example.com")

        assertEquals("userId123", result)
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
        coEvery { remote.isEmailAvailable("available@example.com") } returns true

        val result = userRepository.isEmailAvailable("available@example.com")

        assertTrue(result)
    }

    @Test
    fun isEmailAvailableReturnsFalseWhenNotAvailable() = runTest {
        coEvery { remote.isEmailAvailable("taken@example.com") } returns false

        val result = userRepository.isEmailAvailable("taken@example.com")

        assertFalse(result)
    }

}
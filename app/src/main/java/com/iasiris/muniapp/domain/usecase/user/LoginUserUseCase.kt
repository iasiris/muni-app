package com.iasiris.muniapp.domain.usecase.user

import com.iasiris.muniapp.domain.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): String? {
        return userRepository.loginUser(email, password)
    }
}
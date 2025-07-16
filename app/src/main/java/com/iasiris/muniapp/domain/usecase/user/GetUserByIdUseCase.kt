package com.iasiris.muniapp.domain.usecase.user

import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.repository.UserRepository
import jakarta.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): User? = userRepository.getUserByUserId(userId)
}
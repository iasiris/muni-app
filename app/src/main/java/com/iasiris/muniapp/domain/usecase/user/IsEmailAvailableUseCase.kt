package com.iasiris.muniapp.domain.usecase.user

import com.iasiris.muniapp.domain.repository.UserRepository
import javax.inject.Inject

class IsEmailAvailableUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Boolean = userRepository.isEmailAvailable(email)

}
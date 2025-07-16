package com.iasiris.muniapp.domain.usecase.user

import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.repository.UserRepository

class GetUserByIdUserCase (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): User? = userRepository.getUserByUserId(userId)
}
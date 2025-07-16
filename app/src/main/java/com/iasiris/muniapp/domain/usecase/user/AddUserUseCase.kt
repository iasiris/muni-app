package com.iasiris.muniapp.domain.usecase.user

import com.iasiris.muniapp.domain.model.User
import com.iasiris.muniapp.domain.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Boolean = userRepository.insertUser(user)
}
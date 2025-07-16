package com.iasiris.muniapp.domain.mapper

import com.iasiris.muniapp.data.remote.dto.UserDto
import com.iasiris.muniapp.domain.model.User

fun UserDto.userDtoToDomain() = User(
    id = id,
    email = email,
    password = password,
    fullName = fullName,
    userImageUrl = userImageUrl,
    nationality = nationality
)

fun User.userToDto() = UserDto(
    id = id,
    email = email,
    password = password,
    fullName = fullName,
    userImageUrl = userImageUrl,
    nationality = nationality
)

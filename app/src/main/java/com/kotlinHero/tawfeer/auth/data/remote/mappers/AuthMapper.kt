package com.kotlinHero.tawfeer.auth.data.remote.mappers

import com.kotlinHero.tawfeer.auth.data.local.preferences.UserPreferences
import com.kotlinHero.tawfeer.auth.data.remote.models.ProfileDto
import com.kotlinHero.tawfeer.auth.data.remote.models.login.LoginResponseDto
import com.kotlinHero.tawfeer.auth.domain.models.Profile

fun ProfileDto.toProfile() = Profile(
    email = email,
    fullName = fullName
)

fun LoginResponseDto.toUserPreferences() = UserPreferences(
    id = id,
    username = username,
    email = email,
    firstname = firstname,
    lastname = lastname,
    gender = gender,
    image = image
)
package com.kotlinHero.tawfeer.auth.domain.usecases

import com.kotlinHero.tawfeer.auth.data.local.repositories.UserPreferencesRepository
import com.kotlinHero.tawfeer.auth.data.remote.mappers.toUserPreferences
import com.kotlinHero.tawfeer.auth.data.remote.repositories.AuthRemoteRepository
import com.kotlinHero.tawfeer.common.data.repositories.local.AppPreferencesRepository

class LoginUseCase(
    private val authRemoteRepository: AuthRemoteRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appPreferencesRepository: AppPreferencesRepository,
) {
    suspend operator fun invoke(username: String, password: String) = runCatching {
        val loginResponseDto = authRemoteRepository.login(username = username, password = password)
        val userPreferences = loginResponseDto.toUserPreferences()
        userPreferencesRepository.update { userPreferences }
        val token = loginResponseDto.token
        appPreferencesRepository.setToken(token)
        appPreferencesRepository.setLoggedIn()
    }
}
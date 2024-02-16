package com.kotlinHero.tawfeer.auth.domain.usecases

import com.kotlinHero.tawfeer.auth.data.remote.mappers.toProfile
import com.kotlinHero.tawfeer.auth.data.remote.repositories.AuthRemoteRepository

class GetProfileUseCase(private val authRemoteRepository: AuthRemoteRepository) {
    suspend operator fun invoke() = runCatching {
        authRemoteRepository.getProfile().toProfile()
    }
}
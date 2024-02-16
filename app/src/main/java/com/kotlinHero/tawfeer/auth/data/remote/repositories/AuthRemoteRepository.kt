package com.kotlinHero.tawfeer.auth.data.remote.repositories

import com.kotlinHero.tawfeer.auth.data.remote.api.AuthApi
import com.kotlinHero.tawfeer.auth.data.remote.models.login.LoginRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Dispatchers should be injected
// TODO: Repositories and APIs should have interfaces and be bound with a child

class AuthRemoteRepository(private val authApi: AuthApi) {
    suspend fun getProfile() = withContext(Dispatchers.IO) {
        authApi.getProfile()
    }

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        val loginRequestDto = LoginRequestDto(username = username, password = password)
        authApi.login(loginRequestDto)
    }
}
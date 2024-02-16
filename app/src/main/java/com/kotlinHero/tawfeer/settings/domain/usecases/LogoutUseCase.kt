package com.kotlinHero.tawfeer.settings.domain.usecases

import com.kotlinHero.tawfeer.common.data.repositories.local.AppPreferencesRepository
import com.kotlinHero.tawfeer.common.utils.clearBearerToken
import com.kotlinHero.tawfeer.products.data.local.repositories.CartPreferencesRepository
import io.ktor.client.HttpClient

class LogoutUseCase(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val cartPreferencesRepository: CartPreferencesRepository,
    private val httpClient: HttpClient,
) {
    suspend operator fun invoke() {
        appPreferencesRepository.setLoggedOut()
        appPreferencesRepository.clearToken()
        httpClient.clearBearerToken()
        cartPreferencesRepository.update { it.copy(products = emptyMap()) }
    }
}
package com.kotlinHero.tawfeer.auth.data.interceptors

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthorizationInterceptor {
    private val _unAuthorizedFlow = MutableSharedFlow<Unit>()
    val unAuthorizedFlow = _unAuthorizedFlow.asSharedFlow()

    suspend operator fun invoke(httpResponse: HttpResponse): HttpResponse {
        when(httpResponse.status.value) {
            401 -> _unAuthorizedFlow.emit(Unit)
            else -> Unit
        }
        return httpResponse
    }
}
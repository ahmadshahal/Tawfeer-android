package com.kotlinHero.tawfeer.common.di

import com.kotlinHero.tawfeer.common.data.exceptions.ServerException
import com.kotlinHero.tawfeer.auth.data.interceptors.AuthorizationInterceptor
import com.kotlinHero.tawfeer.common.data.repositories.local.AppPreferencesRepository
import com.kotlinHero.tawfeer.common.data.responses.KtorErrorBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import timber.log.Timber

val KtorModule = module {
    single {
        val authorizationInterceptor = get<AuthorizationInterceptor>()

        HttpClient(Android) {
            expectSuccess = true

            HttpResponseValidator {
                validateResponse {
                    /**
                     * NOTE: This only validates successful responses.
                     */
                    // Add Interceptors here
                }
                handleResponseExceptionWithRequest { exception, _ ->
                    val clientException = exception as? ClientRequestException
                        ?: return@handleResponseExceptionWithRequest

                    /**
                     * Validating the Response for authorization..
                     * NOTE: This only validates failed responses.
                     */
                    authorizationInterceptor(httpResponse = clientException.response)

                    val exceptionResponse = clientException.response.body<KtorErrorBody>()
                    throw ServerException(
                        message = exceptionResponse.message,
                        code = clientException.response.status.value
                    )
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            engine {
                connectTimeout = 10_000
                socketTimeout = 10_000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d("Logger Ktor => $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            Auth {
                bearer {
                    loadTokens {
                        val appPreferencesRepository: AppPreferencesRepository = get()
                        val token = appPreferencesRepository.getToken() ?: ""
                        BearerTokens(
                            accessToken = token,
                            refreshToken = token,
                        )
                    }
                    refreshTokens {
                        val appPreferencesRepository: AppPreferencesRepository = get()
                        val token = appPreferencesRepository.getToken() ?: ""
                        BearerTokens(
                            accessToken = token,
                            refreshToken = token,
                        )
                    }
                }
            }
        }
    }
}
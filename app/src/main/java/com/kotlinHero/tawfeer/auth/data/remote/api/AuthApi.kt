package com.kotlinHero.tawfeer.auth.data.remote.api

import com.kotlinHero.tawfeer.auth.data.remote.models.ProfileDto
import com.kotlinHero.tawfeer.auth.data.remote.models.login.LoginRequestDto
import com.kotlinHero.tawfeer.auth.data.remote.models.login.LoginResponseDto
import com.kotlinHero.tawfeer.common.data.remote.baseURL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class AuthApi(private val httpClient: HttpClient) {
    suspend fun getProfile(): ProfileDto =
        httpClient.get("$baseURL/auth/profile").body()

    suspend fun login(loginRequestDto: LoginRequestDto) = httpClient.post {
        url("$baseURL/auth/login")
        setBody(loginRequestDto)
    }.body<LoginResponseDto>()
}
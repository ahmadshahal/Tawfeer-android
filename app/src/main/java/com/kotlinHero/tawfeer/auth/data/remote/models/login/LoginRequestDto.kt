package com.kotlinHero.tawfeer.auth.data.remote.models.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)
package com.kotlinHero.tawfeer.auth.data.remote.models.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("firstName")
    val firstname: String,
    @SerialName("lastName")
    val lastname: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("image")
    val image: String,
    @SerialName("token")
    val token: String,
)
package com.kotlinHero.tawfeer.auth.data.local.preferences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("username")
    val username: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("firstName")
    val firstname: String = "",
    @SerialName("lastName")
    val lastname: String = "",
    @SerialName("gender")
    val gender: String = "",
    @SerialName("image")
    val image: String = "",
)

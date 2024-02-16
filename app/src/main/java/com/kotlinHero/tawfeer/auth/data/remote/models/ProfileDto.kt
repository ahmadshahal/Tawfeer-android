package com.kotlinHero.tawfeer.auth.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("email")
    val email: String,
    @SerialName("fullName")
    val fullName: String
)
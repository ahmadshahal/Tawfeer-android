package com.kotlinHero.tawfeer.common.data.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KtorErrorBody(
    @SerialName("message")
    val message: String,
)

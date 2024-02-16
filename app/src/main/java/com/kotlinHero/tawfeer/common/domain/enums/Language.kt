package com.kotlinHero.tawfeer.common.domain.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Language(val languageCode: String, val key: String) {
    @SerialName("english")
    English("en", "english"),
    @SerialName("arabic")
    Arabic("ar", "arabic")
}
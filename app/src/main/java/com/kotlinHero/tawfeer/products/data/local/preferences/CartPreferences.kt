package com.kotlinHero.tawfeer.products.data.local.preferences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartPreferences(
    @SerialName("products")
    val products: Map<Int, Int> = emptyMap(),
)

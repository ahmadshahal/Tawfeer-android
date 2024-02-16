package com.kotlinHero.tawfeer.products.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsDto(
    @SerialName("products")
    val products: List<ProductDto>,
    @SerialName("total")
    val total: String,
    @SerialName("skip")
    val skip: String,
    @SerialName("limit")
    val limit: String,
)

package com.kotlinHero.tawfeer.products.domain.models

import com.kotlinHero.tawfeer.products.data.remote.models.ProductDto
import kotlinx.serialization.SerialName

data class Products(
    val products: List<Product>,
    val total: String,
    val skip: String,
    val limit: String,
)
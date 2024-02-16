package com.kotlinHero.tawfeer.products.domain.models

data class ProductWithCart(
    val product: Product,
    val inCart: Int
)

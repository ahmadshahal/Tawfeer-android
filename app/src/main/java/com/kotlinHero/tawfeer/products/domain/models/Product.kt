package com.kotlinHero.tawfeer.products.domain.models

import kotlin.math.ceil

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val discount: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
) {
    val priceAfterDiscount = ceil(price * discount / 100)
}
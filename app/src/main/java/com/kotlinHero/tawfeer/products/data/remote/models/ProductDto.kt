package com.kotlinHero.tawfeer.products.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Float,
    @SerialName("discountPercentage")
    val discount: Float,
    @SerialName("rating")
    val rating: Float,
    @SerialName("stock")
    val stock: Int,
    @SerialName("brand")
    val brand: String,
    @SerialName("category")
    val category: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("images")
    val images: List<String>,
)

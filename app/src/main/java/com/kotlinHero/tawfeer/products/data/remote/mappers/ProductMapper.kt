package com.kotlinHero.tawfeer.products.data.remote.mappers

import com.kotlinHero.tawfeer.products.data.remote.models.ProductDto
import com.kotlinHero.tawfeer.products.data.remote.models.ProductsDto
import com.kotlinHero.tawfeer.products.domain.models.Product
import com.kotlinHero.tawfeer.products.domain.models.Products

fun ProductDto.toProduct() = Product(
    id = id,
    title = title,
    description = description,
    price = price,
    discount = discount,
    rating = rating,
    stock = stock,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    images = images
)

fun ProductsDto.toProducts() = Products(
    products = products.map { it.toProduct() },
    skip = skip,
    limit = limit,
    total = total
)
package com.kotlinHero.tawfeer.products.data.remote.api

import com.kotlinHero.tawfeer.common.data.remote.baseURL
import com.kotlinHero.tawfeer.products.data.remote.models.ProductDto
import com.kotlinHero.tawfeer.products.data.remote.models.ProductsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class ProductsApi(private val httpClient: HttpClient) {
    suspend fun getProducts(skip: Int = 0, limit: Int = 20): ProductsDto =
        httpClient.get {
            url("$baseURL/products?limit=$limit&skip=$skip")
        }.body()

    suspend fun getProduct(id: Int): ProductDto =
        httpClient.get {
            url("$baseURL/products/$id")
        }.body()
}
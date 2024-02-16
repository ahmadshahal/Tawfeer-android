package com.kotlinHero.tawfeer.products.domain.usecases

import com.kotlinHero.tawfeer.products.data.remote.mappers.toProduct
import com.kotlinHero.tawfeer.products.data.remote.repositories.ProductsRemoteRepository

class GetProductDetailsUseCase(private val productsRemoteRepository: ProductsRemoteRepository) {
    suspend operator fun invoke(id: Int) = runCatching {
        productsRemoteRepository.getProduct(id).toProduct()
    }
}
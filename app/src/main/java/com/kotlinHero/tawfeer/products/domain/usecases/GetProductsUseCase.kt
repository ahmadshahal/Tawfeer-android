package com.kotlinHero.tawfeer.products.domain.usecases

import androidx.paging.map
import com.kotlinHero.tawfeer.products.data.remote.mappers.toProduct
import com.kotlinHero.tawfeer.products.data.remote.repositories.ProductsRemoteRepository
import kotlinx.coroutines.flow.map

class GetProductsUseCase(private val productsRemoteRepository: ProductsRemoteRepository) {
    operator fun invoke() =
        productsRemoteRepository.getProducts()
            .map { it.map { productDto -> productDto.toProduct() } }
}
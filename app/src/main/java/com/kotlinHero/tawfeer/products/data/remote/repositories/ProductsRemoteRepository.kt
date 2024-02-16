package com.kotlinHero.tawfeer.products.data.remote.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kotlinHero.tawfeer.products.data.remote.api.ProductsApi
import com.kotlinHero.tawfeer.products.data.remote.models.ProductDto
import com.kotlinHero.tawfeer.products.data.remote.pagination.ProductsPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


// TODO: Dispatchers should be injected
// TODO: Repositories and APIs should have interfaces and be bound with a child

private val NETWORK_PAGE_SIZE = 10

class ProductsRemoteRepository(private val productsApi: ProductsApi) {
    suspend fun getProducts(skip: Int, limit: Int) = withContext(Dispatchers.IO) {
        productsApi.getProducts(skip = skip, limit = limit)
    }

    suspend fun getProduct(id: Int) = withContext(Dispatchers.IO) {
        productsApi.getProduct(id = id)
    }

    fun getProducts(): Flow<PagingData<ProductDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductsPagingSource(productsApi = productsApi) }
        ).flow
    }
}
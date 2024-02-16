package com.kotlinHero.tawfeer.products.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotlinHero.tawfeer.products.data.remote.api.ProductsApi
import com.kotlinHero.tawfeer.products.data.remote.models.ProductDto

private val NETWORK_PAGE_SIZE = 10

class ProductsPagingSource(private val productsApi: ProductsApi) : PagingSource<Int, ProductDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDto> {
        val pageIndex = params.key ?: 1
        val result = runCatching {
            val response = productsApi.getProducts(
                skip = pageIndex * NETWORK_PAGE_SIZE,
                limit = NETWORK_PAGE_SIZE
            )
            response.products
        }
        return result.fold(
            onSuccess = { products ->
                val nextKey = if (products.isEmpty()) null else pageIndex + 1
                LoadResult.Page(
                    data = products,
                    prevKey = if (pageIndex == 1) null else pageIndex,
                    nextKey = nextKey
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ProductDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
package com.kotlinHero.tawfeer.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.products.domain.usecases.GetProductsUseCase
import com.kotlinHero.tawfeer.products.ui.states.ProductsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ProductsViewModel(private val getProductsUseCase: GetProductsUseCase) : ViewModel() {
    private val _productsStateFlow = MutableStateFlow(ProductsState())
    val productsStateFlow = _productsStateFlow.asStateFlow()

    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    val productsFlow = getProductsUseCase().cachedIn(
        scope = viewModelScope,
    )

    /*
    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _productsStateFlow.update { it.copy(fetchState = FetchState.Loading) }
            val result = getProductsUseCase(skip = 0, limit = 20)
            result.fold(
                onSuccess = { products ->
                    _productsStateFlow.update {
                        it.copy(fetchState = FetchState.Success(products))
                    }
                },
                onFailure = { exception ->
                    val action = ChannelAction.ShowSnackBar(exception.toSnackBarEvent())
                    _channel.send(action)
                    _productsStateFlow.update {
                        it.copy(fetchState = FetchState.Error(exception.message ?: ""))
                    }
                }
            )
        }
    }
     */
}
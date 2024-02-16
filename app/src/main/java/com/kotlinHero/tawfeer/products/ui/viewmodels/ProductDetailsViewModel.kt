package com.kotlinHero.tawfeer.products.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinHero.tawfeer.common.states.FetchState
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.toSnackBarEvent
import com.kotlinHero.tawfeer.products.domain.usecases.GetProductDetailsUseCase
import com.kotlinHero.tawfeer.products.ui.states.ProductDetailsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _productDetailsStateFlow = MutableStateFlow(ProductDetailsState())
    val productDetailsStateFlow = _productDetailsStateFlow.asStateFlow()

    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    private val productId = savedStateHandle.get<Int>("productId") ?: 0

    init {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            _productDetailsStateFlow.update { it.copy(fetchState = FetchState.Loading) }
            val result = getProductDetailsUseCase(id = productId)
            result.fold(
                onSuccess = { product ->
                    _productDetailsStateFlow.update {
                        it.copy(fetchState = FetchState.Success(product))
                    }
                },
                onFailure = { exception ->
                    val action = ChannelAction.ShowSnackBar(exception.toSnackBarEvent())
                    _channel.send(action)
                    _productDetailsStateFlow.update {
                        it.copy(fetchState = FetchState.Error(exception.message ?: ""))
                    }
                }
            )
        }
    }
}
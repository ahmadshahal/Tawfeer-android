package com.kotlinHero.tawfeer.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.states.FetchState
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.SnackBarEvent
import com.kotlinHero.tawfeer.common.utils.UiText
import com.kotlinHero.tawfeer.common.utils.toSnackBarEvent
import com.kotlinHero.tawfeer.products.data.local.repositories.CartPreferencesRepository
import com.kotlinHero.tawfeer.products.domain.models.Product
import com.kotlinHero.tawfeer.products.domain.models.ProductWithCart
import com.kotlinHero.tawfeer.products.domain.usecases.GetProductDetailsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartPreferencesRepository: CartPreferencesRepository,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
) : ViewModel() {
    private val productsFlow = MutableStateFlow(emptyList<Product>())

    private val deque = ArrayDeque<ProductWithCart>()

    private val _fetchStateFlow = MutableStateFlow<FetchState>(FetchState.Initial)
    val fetchStateFlow = _fetchStateFlow.asStateFlow()

    val cartProductsFlow =
        combine(cartPreferencesRepository.cartPreferencesFlow, productsFlow) { cart, products ->
            products
                .map { ProductWithCart(inCart = cart.products[it.id] ?: 0, product = it) }
                .filter { it.inCart > 0 }
        }

    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    init {
        syncProducts()
    }

    fun checkout() {
        viewModelScope.launch {
            cartPreferencesRepository.update { it.copy(products = emptyMap()) }
            val action = ChannelAction.Navigate { navController ->
                navController.popBackStack()
            }
            _channel.send(action)
        }
    }

    private fun syncProducts() {
        viewModelScope.launch {
            _fetchStateFlow.update { FetchState.Loading }
            val products = mutableListOf<Product>()
            val ids = cartPreferencesRepository.cartPreferencesFlow.first().products.keys.toList()
            val tasks = ids.map { async { getProductDetailsUseCase(it) } }
            val awaitedTasks = tasks.awaitAll()
            awaitedTasks.forEach { result ->
                result.fold(
                    onSuccess = { product ->
                        products.add(product)
                    },
                    onFailure = { throwable ->
                        val action =
                            ChannelAction.ShowSnackBar(snackBarEvent = throwable.toSnackBarEvent())
                        _channel.send(action)
                        _fetchStateFlow.update { FetchState.Error(throwable.message ?: "") }
                        return@launch
                    }
                )
            }
            productsFlow.emit(products)
            _fetchStateFlow.update { FetchState.Success(Unit) }
        }
    }

    fun onPlusClicked(product: ProductWithCart) {
        viewModelScope.launch {
            val cartPreferences = cartPreferencesRepository.cartPreferencesFlow.first()
            val mutableMap = cartPreferences.products.toMutableMap()
            mutableMap[product.product.id] = (mutableMap[product.product.id] ?: 0) + 1
            cartPreferencesRepository.update { it.copy(products = mutableMap.toMap()) }
        }
    }

    fun onMinusClicked(product: ProductWithCart) {
        viewModelScope.launch {
            val cartPreferences = cartPreferencesRepository.cartPreferencesFlow.first()
            val mutableMap = cartPreferences.products.toMutableMap()
            if ((mutableMap[product.product.id] ?: 0) - 1 <= 0) return@launch
            mutableMap[product.product.id] = (mutableMap[product.product.id] ?: 0) - 1
            cartPreferencesRepository.update { it.copy(products = mutableMap.toMap()) }
        }
    }

    fun onRemoveClicked(product: ProductWithCart) {
        viewModelScope.launch {
            deque.add(product)
            val cartPreferences = cartPreferencesRepository.cartPreferencesFlow.first()
            val mutableMap = cartPreferences.products.toMutableMap()
            mutableMap.remove(product.product.id)
            cartPreferencesRepository.update { it.copy(products = mutableMap.toMap()) }
            val action = ChannelAction.ShowSnackBar(
                SnackBarEvent.Confirmation(UiText.StringResource(R.string.product_removed_from_cart))
            )
            _channel.send(action)
        }
    }

    fun restoreLastProduct() {
        viewModelScope.launch {
            val product = deque.removeLast()
            val cartPreferences = cartPreferencesRepository.cartPreferencesFlow.first()
            val mutableMap = cartPreferences.products.toMutableMap()
            mutableMap[product.product.id] = product.inCart
            cartPreferencesRepository.update { it.copy(products = mutableMap.toMap()) }
        }
    }
}
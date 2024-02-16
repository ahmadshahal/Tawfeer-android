package com.kotlinHero.tawfeer.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.products.data.local.repositories.CartPreferencesRepository
import com.kotlinHero.tawfeer.products.domain.models.Product
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProductDialogViewModel(
    private val cartPreferencesRepository: CartPreferencesRepository,
) : ViewModel() {
    val cartPreferencesFlow = cartPreferencesRepository.cartPreferencesFlow

    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    fun addToCart(product: Product, amount: Int) {
        viewModelScope.launch {
            val cartPreferences = cartPreferencesFlow.first()
            val mutableMap = cartPreferences.products.toMutableMap()
            mutableMap[product.id] = (mutableMap[product.id] ?: 0) + amount
            cartPreferencesRepository.update { it.copy(products = mutableMap.toMap()) }
            _channel.send(ChannelAction.CleanUp)
        }
    }

}
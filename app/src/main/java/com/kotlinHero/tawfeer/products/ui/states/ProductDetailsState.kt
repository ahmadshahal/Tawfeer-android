package com.kotlinHero.tawfeer.products.ui.states

import com.kotlinHero.tawfeer.common.states.FetchState

data class ProductDetailsState(
    val fetchState: FetchState = FetchState.Initial,
)

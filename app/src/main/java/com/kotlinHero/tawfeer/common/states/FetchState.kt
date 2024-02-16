package com.kotlinHero.tawfeer.common.states

sealed class FetchState {
    data class Success<T>(val result: T) : FetchState()
    data class Error(val message: String) : FetchState()
    data object Loading : FetchState()
    data object Initial : FetchState()
}

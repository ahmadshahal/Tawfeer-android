package com.kotlinHero.tawfeer.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.auth.data.interceptors.AuthorizationInterceptor
import com.kotlinHero.tawfeer.common.data.repositories.local.AppPreferencesRepository
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val authorizationInterceptor: AuthorizationInterceptor,
) : ViewModel() {
    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    var splashScreenVisible = true
        private set

    init {
        setStartDestination()
        handleUnAuthorization()
    }

    private fun setStartDestination() {
        viewModelScope.launch {
            val isLoggedIn = appPreferencesRepository.isLoggedIn() ?: false
            if(isLoggedIn) {
                val action = ChannelAction.Navigate { navController ->
                    navController.navigate(R.id.action_LoginFragment_to_ProductsFragment)
                }
                _channel.send(action)
            }
            delay(2000)
            splashScreenVisible = false
        }
    }

    private fun handleUnAuthorization() {
        viewModelScope.launch {
            authorizationInterceptor.unAuthorizedFlow.collect {
                val action = ChannelAction.Navigate { navController ->
                    navController.navigate(R.id.action_to_LoginFragment)
                }
                _channel.send(action)
            }
        }
    }
}
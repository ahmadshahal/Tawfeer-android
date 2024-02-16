package com.kotlinHero.tawfeer.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.data.repositories.local.AppPreferencesRepository
import com.kotlinHero.tawfeer.common.domain.enums.Language
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.settings.domain.usecases.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val appPreferencesRepository: AppPreferencesRepository,
) : ViewModel() {
    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    val languageFlow = appPreferencesRepository.languageFlow()

    fun onChangeLanguage(language: Language) {
        viewModelScope.launch {
            appPreferencesRepository.setLanguage(language)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            val action = ChannelAction.Navigate { navController ->
                navController.navigate(R.id.action_to_LoginFragment)
            }
            _channel.send(action)
        }
    }
}
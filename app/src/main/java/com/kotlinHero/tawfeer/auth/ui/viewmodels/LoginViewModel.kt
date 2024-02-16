package com.kotlinHero.tawfeer.auth.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.auth.domain.usecases.LoginUseCase
import com.kotlinHero.tawfeer.auth.ui.states.LoginState
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.UiText
import com.kotlinHero.tawfeer.common.utils.toSnackBarEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _loginStateFlow = MutableStateFlow(LoginState())
    val loginStateFlow = _loginStateFlow.asStateFlow()

    private val _channel = Channel<ChannelAction>()
    val channel = _channel.receiveAsFlow()

    fun login() {
        viewModelScope.launch {
            validate()
            val loginState = _loginStateFlow.first()
            val isValid = loginState.usernameFormValueState.isValid
                    && loginState.passwordFormValueState.isValid
            if(!isValid) return@launch

            _loginStateFlow.update { it.copy(isLoading = true) }

            val username = loginState.usernameFormValueState.value
            val password = loginState.passwordFormValueState.value
            val result = loginUseCase(username = username, password = password)
            result.fold(
                onSuccess = {
                    val action = ChannelAction.Navigate { navController ->
                        navController.navigate(R.id.action_LoginFragment_to_ProductsFragment)
                    }
                    _channel.send(action)
                },
                onFailure = {
                    val action = ChannelAction.ShowSnackBar(snackBarEvent = it.toSnackBarEvent())
                    _channel.send(action)
                }
            )
            _loginStateFlow.update { it.copy(isLoading = false) }
        }
    }

    fun onUsernameChange(username: String) {
        _loginStateFlow.update {
            it.usernameFormCopy(username = username)
        }
    }

    fun onPasswordChange(password: String) {
        _loginStateFlow.update {
            it.passwordFormCopy(password = password)
        }
    }

    fun onPasswordObscuredChange(passwordObscured: Boolean) {
        _loginStateFlow.update {
            it.copy(passwordObscured = passwordObscured)
        }
    }

    private suspend fun validate() {
        val loginState = _loginStateFlow.first()
        val username = loginState.usernameFormValueState.value
        val password = loginState.passwordFormValueState.value
        validateUsername(username)
        validatePassword(password)
    }

    private fun validateUsername(username: String) {
        val isValid = username.isNotEmpty()
        val message = UiText.StringResource(R.string.username_can_t_be_empty)
        _loginStateFlow.update {
            it.usernameFormCopy(isValid = isValid, validationMessage = message)
        }
    }

    private fun validatePassword(password: String) {
        val isValid = password.isNotEmpty()
        val message = UiText.StringResource(R.string.password_can_t_be_empty)
        _loginStateFlow.update {
            it.passwordFormCopy(isValid = isValid, validationMessage = message)
        }
    }
}
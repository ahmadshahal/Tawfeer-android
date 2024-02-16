package com.kotlinHero.tawfeer.auth.ui.states

import com.kotlinHero.tawfeer.common.states.FormValueState
import com.kotlinHero.tawfeer.common.utils.UiText

data class LoginState(
    val isLoading: Boolean = false,
    val passwordObscured: Boolean = true,
    val usernameFormValueState: FormValueState<String> = FormValueState(""),
    val passwordFormValueState: FormValueState<String> = FormValueState(""),
) {
    fun usernameFormCopy(
        username: String? = null,
        isValid: Boolean? = null,
        validationMessage: UiText? = null,
    ) =
        copy(
            usernameFormValueState = usernameFormValueState.copy(
                value = username ?: usernameFormValueState.value,
                isValid = isValid ?: usernameFormValueState.isValid,
                validationMessage = validationMessage ?: usernameFormValueState.validationMessage
            )
        )

    fun passwordFormCopy(
        password: String? = null,
        isValid: Boolean? = null,
        validationMessage: UiText? = null,
    ) =
        copy(
            passwordFormValueState = passwordFormValueState.copy(
                value = password ?: passwordFormValueState.value,
                isValid = isValid ?: passwordFormValueState.isValid,
                validationMessage = validationMessage ?: passwordFormValueState.validationMessage
            )
        )
}

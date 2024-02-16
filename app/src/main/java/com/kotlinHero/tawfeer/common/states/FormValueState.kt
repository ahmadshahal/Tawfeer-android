package com.kotlinHero.tawfeer.common.states

import com.kotlinHero.tawfeer.common.utils.UiText

data class FormValueState<T>(
    val value: T,
    val isValid: Boolean = true,
    val validationMessage: UiText? = null,
)

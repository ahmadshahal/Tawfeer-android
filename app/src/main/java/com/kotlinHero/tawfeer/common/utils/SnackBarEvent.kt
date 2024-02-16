package com.kotlinHero.tawfeer.common.utils

import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.data.exceptions.ServerException

sealed class SnackBarEvent(val message: UiText) {
    class ConnectionError(message: UiText) : SnackBarEvent(message)
    class ServerError(message: UiText) : SnackBarEvent(message)
    class Confirmation(message: UiText) : SnackBarEvent(message)
}

fun Throwable.toSnackBarEvent() = when(this) {
    is ServerException -> SnackBarEvent.ServerError(message = UiText.DynamicString(this.message ?: ""))
    else -> SnackBarEvent.ConnectionError(message = UiText.StringResource(R.string.could_not_reach_remote_server))
}



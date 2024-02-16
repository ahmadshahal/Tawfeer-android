package com.kotlinHero.tawfeer.common.data.exceptions

class ServerException(
    message: String,
    val code: Int,
) : Exception(message)
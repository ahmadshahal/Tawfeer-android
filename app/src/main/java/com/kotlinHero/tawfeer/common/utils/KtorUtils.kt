package com.kotlinHero.tawfeer.common.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin

fun HttpClient.clearBearerToken() = plugin(Auth)
    .providers
    .filterIsInstance<BearerAuthProvider>()
    .firstOrNull()?.clearToken()
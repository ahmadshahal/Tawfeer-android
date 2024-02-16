package com.kotlinHero.tawfeer.common.data.repositories.mappers

import com.kotlinHero.tawfeer.common.domain.enums.Language

fun String.toLanguage() = Language.values().find { it.key == this } ?: Language.English
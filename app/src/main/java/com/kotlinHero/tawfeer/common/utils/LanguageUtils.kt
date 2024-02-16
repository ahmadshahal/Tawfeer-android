package com.kotlinHero.tawfeer.common.utils

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.kotlinHero.tawfeer.common.domain.enums.Language
import java.util.Locale

fun Context.changeLanguage(languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList(Locale.forLanguageTag(languageCode))
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }
}

fun Context.getLanguage(): Language {
    val languageCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
    } else {
        AppCompatDelegate.getApplicationLocales().toLanguageTags()
    }
    return Language.values().find { it.languageCode == languageCode } ?: Language.English
}

fun Context.getCurrentLocale(): Locale {
    return resources.configuration.locales[0]
}

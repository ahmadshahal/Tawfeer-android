package com.kotlinHero.tawfeer.common.data.repositories.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kotlinHero.tawfeer.common.data.repositories.mappers.toLanguage
import com.kotlinHero.tawfeer.common.domain.enums.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val tokenKey = stringPreferencesKey("token")
private val isLoggedInKey = booleanPreferencesKey("is_logged_in")
private val languageKey = stringPreferencesKey("language")

class AppPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    suspend fun getToken() = dataStore.data
        .map { preferences -> preferences[tokenKey] }
        .first()

    suspend fun setToken(token: String) = dataStore.edit { preferences ->
        preferences[tokenKey] = token
    }

    suspend fun clearToken() = dataStore.edit { preferences ->
        preferences.remove(tokenKey)
    }

    suspend fun setLanguage(language: Language) = dataStore.edit { preferences ->
        preferences[languageKey] = language.key
    }

    suspend fun getLanguage() = dataStore.data
            .map { it[languageKey]?.toLanguage() ?: Language.English }
            .first()

    fun languageFlow() = dataStore.data
        .map { it[languageKey]?.toLanguage() ?: Language.English }

    fun isLoggedInFlow() = dataStore.data.map { it[isLoggedInKey] }

    suspend fun isLoggedIn() = dataStore.data
        .map { preferences -> preferences[isLoggedInKey] }
        .first()

    suspend fun setLoggedIn() = dataStore.edit { preferences ->
        preferences[isLoggedInKey] = true
    }

    suspend fun setLoggedOut() = dataStore.edit { preferences ->
        preferences[isLoggedInKey] = false
    }
}
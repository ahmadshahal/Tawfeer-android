package com.kotlinHero.tawfeer.auth.data.local.repositories

import androidx.datastore.core.DataStore
import com.kotlinHero.tawfeer.auth.data.local.preferences.UserPreferences

class UserPreferencesRepository(private val dataStore: DataStore<UserPreferences>) {
    val userPreferencesFlow = dataStore.data

    suspend fun update(transform: suspend (UserPreferences) -> UserPreferences) {
        dataStore.updateData(transform)
    }
}
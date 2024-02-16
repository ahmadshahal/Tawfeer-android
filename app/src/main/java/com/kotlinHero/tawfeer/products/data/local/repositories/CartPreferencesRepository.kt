package com.kotlinHero.tawfeer.products.data.local.repositories

import androidx.datastore.core.DataStore
import com.kotlinHero.tawfeer.products.data.local.preferences.CartPreferences

class CartPreferencesRepository(private val dataStore: DataStore<CartPreferences>) {
    val cartPreferencesFlow = dataStore.data

    suspend fun update(transform: suspend (CartPreferences) -> CartPreferences) {
        dataStore.updateData(transform)
    }
}
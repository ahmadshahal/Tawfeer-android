package com.kotlinHero.tawfeer.common.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kotlinHero.tawfeer.common.data.repositories.local.AppPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val CommonModule = module {
    single {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { androidContext().preferencesDataStoreFile("app_preferences") }
        )
    }
    factory { AppPreferencesRepository(get()) }
}
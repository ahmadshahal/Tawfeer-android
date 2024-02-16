package com.kotlinHero.tawfeer.auth.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.kotlinHero.tawfeer.auth.data.interceptors.AuthorizationInterceptor
import com.kotlinHero.tawfeer.auth.data.local.preferences.UserPreferences
import com.kotlinHero.tawfeer.auth.data.local.repositories.UserPreferencesRepository
import com.kotlinHero.tawfeer.auth.data.local.serializers.UserPreferencesSerializer
import com.kotlinHero.tawfeer.auth.data.remote.api.AuthApi
import com.kotlinHero.tawfeer.auth.data.remote.repositories.AuthRemoteRepository
import com.kotlinHero.tawfeer.auth.domain.usecases.GetProfileUseCase
import com.kotlinHero.tawfeer.auth.domain.usecases.LoginUseCase
import com.kotlinHero.tawfeer.auth.ui.viewmodels.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AuthModule = module {
    single { AuthorizationInterceptor() }
    factory { UserPreferencesRepository(get(named("userPrefDataStore"))) }
    single(named("userPrefDataStore")) {
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { androidContext().dataStoreFile("user_preferences") },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPreferences() }
            ),
        )
    }
    factory { AuthApi(get()) }
    factory { GetProfileUseCase(get()) }
    factory { LoginUseCase(get(), get(), get()) }
    factory { AuthRemoteRepository(get()) }
    viewModel { LoginViewModel(get()) }
}
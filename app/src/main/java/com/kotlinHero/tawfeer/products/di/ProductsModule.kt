package com.kotlinHero.tawfeer.products.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.kotlinHero.tawfeer.products.data.local.preferences.CartPreferences
import com.kotlinHero.tawfeer.products.data.local.repositories.CartPreferencesRepository
import com.kotlinHero.tawfeer.products.data.local.serializers.CartPreferencesSerializer
import com.kotlinHero.tawfeer.products.data.remote.api.ProductsApi
import com.kotlinHero.tawfeer.products.data.remote.repositories.ProductsRemoteRepository
import com.kotlinHero.tawfeer.products.domain.usecases.GetProductDetailsUseCase
import com.kotlinHero.tawfeer.products.domain.usecases.GetProductsUseCase
import com.kotlinHero.tawfeer.products.ui.viewmodels.CartViewModel
import com.kotlinHero.tawfeer.products.ui.viewmodels.ProductDetailsViewModel
import com.kotlinHero.tawfeer.products.ui.viewmodels.ProductDialogViewModel
import com.kotlinHero.tawfeer.products.ui.viewmodels.ProductsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ProductsModule = module {
    factory { CartPreferencesRepository(get(named("cartPrefDataStore"))) }
    single(named("cartPrefDataStore")) {
        DataStoreFactory.create(
            serializer = CartPreferencesSerializer,
            produceFile = { androidContext().dataStoreFile("cart_preferences") },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { CartPreferences() }
            ),
        )
    }
    factory { ProductsApi(get()) }
    factory { ProductsRemoteRepository(get()) }
    factory { GetProductsUseCase(get()) }
    factory { GetProductDetailsUseCase(get()) }
    viewModel { ProductsViewModel(get()) }
    viewModel { ProductDetailsViewModel(get(), get()) }
    viewModel { ProductDialogViewModel(get()) }
    viewModel { CartViewModel(get(), get()) }
}
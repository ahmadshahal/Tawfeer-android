package com.kotlinHero.tawfeer.settings.di

import com.kotlinHero.tawfeer.settings.domain.usecases.LogoutUseCase
import com.kotlinHero.tawfeer.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SettingsModule = module {
    viewModel { SettingsViewModel(get(), get()) }
    factory { LogoutUseCase(get(), get(), get()) }
}
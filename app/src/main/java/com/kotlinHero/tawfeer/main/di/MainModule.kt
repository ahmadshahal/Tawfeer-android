package com.kotlinHero.tawfeer.main.di

import com.kotlinHero.tawfeer.main.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainModule = module {
    viewModel { MainViewModel(get(), get()) }
}
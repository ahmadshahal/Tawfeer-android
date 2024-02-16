package com.kotlinHero.tawfeer

import android.app.Application
import com.kotlinHero.tawfeer.auth.di.AuthModule
import com.kotlinHero.tawfeer.common.di.CommonModule
import com.kotlinHero.tawfeer.common.di.KtorModule
import com.kotlinHero.tawfeer.main.di.MainModule
import com.kotlinHero.tawfeer.products.di.ProductsModule
import com.kotlinHero.tawfeer.settings.di.SettingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TawfeerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger()
            // Injecting the applicationContext to be used when needed as androidContext()
            androidContext(this@TawfeerApplication)
            modules(
                KtorModule,
                AuthModule,
                CommonModule,
                MainModule,
                ProductsModule,
                SettingsModule
            )
        }
    }
}
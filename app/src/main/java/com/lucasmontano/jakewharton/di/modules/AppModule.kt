package com.lucasmontano.jakewharton.di.modules

import com.lucasmontano.jakewharton.JakeApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: JakeApp) {

    @Provides @Singleton
    fun provideApplication() = app
}
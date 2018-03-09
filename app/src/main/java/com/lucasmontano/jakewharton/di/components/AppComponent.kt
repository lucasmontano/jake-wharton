package com.lucasmontano.jakewharton.di.components

import com.lucasmontano.jakewharton.JakeApp
import com.lucasmontano.jakewharton.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun inject(jakeApp: JakeApp)
}
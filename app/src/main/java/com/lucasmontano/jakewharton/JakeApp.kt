package com.lucasmontano.jakewharton

import android.app.Application
import com.lucasmontano.jakewharton.di.components.AppComponent
import com.lucasmontano.jakewharton.di.components.DaggerAppComponent
import com.lucasmontano.jakewharton.di.modules.AppModule

class JakeApp : Application() {

    private val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}

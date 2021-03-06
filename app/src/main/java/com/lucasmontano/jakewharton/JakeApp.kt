package com.lucasmontano.jakewharton

import android.app.Application
import com.lucasmontano.jakewharton.di.components.AppComponent
import com.lucasmontano.jakewharton.di.components.DaggerAppComponent
import com.lucasmontano.jakewharton.di.modules.AppModule
import io.realm.Realm

class JakeApp: Application() {

  val component: AppComponent by lazy {
    DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()
  }

  /**
   * OnCreate init Realm and Dagger.
   */
  override fun onCreate() {
    super.onCreate()
    component.inject(this)
    Realm.init(this)
  }
}

package com.lucasmontano.jakewharton.di.components

import com.lucasmontano.jakewharton.JakeApp
import com.lucasmontano.jakewharton.di.modules.AppModule
import com.lucasmontano.jakewharton.view.activities.MainActivity
import com.lucasmontano.jakewharton.view.fragments.RepoListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

  fun inject(jakeApp: JakeApp)
  fun inject(mainActivity: MainActivity)
  fun inject(fragmentListRepo: RepoListFragment)
}
package com.lucasmontano.jakewharton.di.modules

import com.lucasmontano.jakewharton.JakeApp
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.networking.RepoApiService
import com.lucasmontano.jakewharton.presenter.RepoListPresenter
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule(private val app: JakeApp) {

  @Provides
  @Singleton
  fun provideApplication() = app

  @Provides
  fun provideRepoApiService() = RepoApiService(Realm.getDefaultInstance())

  @Provides
  @Singleton
  fun provideGetRepoInteractor(repoApiService: RepoApiService) = GetRepoInteractor(repoApiService,
      Realm.getDefaultInstance())

  @Provides
  fun provideRepoListPresenter(getRepoInteractor: GetRepoInteractor) = RepoListPresenter(
      getRepoInteractor)
}
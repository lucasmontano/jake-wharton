package com.lucasmontano.jakewharton.di.modules

import com.lucasmontano.jakewharton.JakeApp
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.networking.RepoApiService
import com.lucasmontano.jakewharton.presenter.RepoListPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: JakeApp) {

    @Provides @Singleton
    fun provideApplication() = app

    @Provides
    fun provideRepoApiService() = RepoApiService()

    @Provides @Singleton
    fun provideGetRepoInteractor(repoApiService : RepoApiService) = GetRepoInteractor(repoApiService = repoApiService)

    @Provides
    fun provideRepoListPresenter(getRepoInteractor : GetRepoInteractor) = RepoListPresenter(getRepoInteractor = getRepoInteractor)
}
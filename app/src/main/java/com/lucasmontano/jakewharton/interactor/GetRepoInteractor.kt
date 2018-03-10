package com.lucasmontano.jakewharton.interactor

import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.networking.RepoApiService
import io.reactivex.Observer
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepoInteractor @Inject constructor(private val repoApiService: RepoApiService) {

    /**
     * Get a page of Jake's Repos
     *
     * @param page The page number.
     * @param observer The Observer.
     */
    fun getRepo(page: String, observer: Observer<Result<List<RepoData>>>) {
        repoApiService.getRepo(page).subscribe(observer)
    }
}
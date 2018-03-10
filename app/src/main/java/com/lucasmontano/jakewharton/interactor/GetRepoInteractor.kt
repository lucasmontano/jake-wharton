package com.lucasmontano.jakewharton.interactor

import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.networking.RepoApiService
import io.reactivex.Observer
import retrofit2.Response
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
    fun getRepo(page: String, observer: Observer<Response<ResponseData>>) {
        repoApiService.getRepo(page).subscribe(observer)
    }
}
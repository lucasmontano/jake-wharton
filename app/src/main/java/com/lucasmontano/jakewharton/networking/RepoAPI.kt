package com.lucasmontano.jakewharton.networking

import com.lucasmontano.jakewharton.data.RepoData
import io.reactivex.Observable
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Url

interface RepoAPI {

    @GET
    fun getRepo(@Url url: String): Observable<Result<List<RepoData>>>
}
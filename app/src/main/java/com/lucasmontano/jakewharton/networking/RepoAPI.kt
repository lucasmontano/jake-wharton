package com.lucasmontano.jakewharton.networking

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RepoAPI {

    @GET("repos")
    fun getRepo(@QueryMap map: Map<String, Int>): Observable<Response<RepoResponse>>
}
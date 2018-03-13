package com.lucasmontano.jakewharton.networking

import com.lucasmontano.jakewharton.data.ResponseData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RepoAPI {

  /**
   * Check @see <a href="https://developer.github.com/v3/repos/#list-user-repositories">GitHub Documentation</a>
   * @param url
   */
  @GET
  fun getRepo(@Url url: String): Observable<Response<ResponseData>>
}
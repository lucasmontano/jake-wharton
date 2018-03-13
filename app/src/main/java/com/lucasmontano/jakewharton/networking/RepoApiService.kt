package com.lucasmontano.jakewharton.networking

import android.util.Log
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.data.ResponseData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RepoApiService(private val realm: Realm?) {

  private var repoAPI: RepoAPI = RetrofitAdapterFactory.adapter.create<RepoAPI>(RepoAPI::class.java)

  /**
   * @param url
   */
  fun getRepo(url: String): Observable<Response<ResponseData>> {
    return repoAPI.getRepo(url)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError { t -> onError(t) }
        .doOnNext { r -> r.body()?.repos?.let { processResponse(it) } }
  }

  /**
   * Save RepoData in Realm.
   *
   * @param listRepoData
   */
  private fun processResponse(listRepoData: List<RepoData>) {

    realm?.run {

      beginTransaction()

      listRepoData.forEach {

        copyToRealmOrUpdate(it)
      }

      commitTransaction()
    }
  }

  /**
   * Log error.
   *
   * @param throwable
   */
  private fun onError(throwable: Throwable) {

    if (throwable is HttpException) {

      // We had non-2XX http error
      Log.e("RepoApiServiceError", "HTTPException: " + throwable.code())
    }
    if (throwable is IOException) {

      // A network or conversion error happened
      Log.e("RepoApiServiceError", "IOException: " + throwable.message)
    }
  }
}
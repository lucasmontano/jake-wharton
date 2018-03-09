package com.lucasmontano.jakewharton.networking

import android.util.Log
import com.lucasmontano.jakewharton.data.RepoData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

class RepoApiService(private var retrofit: Retrofit) {

    private var repoAPI: RepoAPI = retrofit.create<RepoAPI>(RepoAPI::class.java)

    fun getRepo(request: RepoRequest) : Observable<List<RepoData>> {
        val mapRequest: Map<String, Int> = hashMapOf("page" to request.page, "per_page" to request.per_page)
        return repoAPI.getRepo(mapRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t -> onError(t) }
                .doOnNext { r -> processResponse(r) }
    }

    private fun processResponse(response: List<RepoData>) {
        TODO("not implemented: Saving RepoResponse in Cache")
    }

    private fun onError(throwable: Throwable) {

        if (throwable is HttpException) {
            // We had non-2XX http error
            Log.e("RepoApiServiceError", "HTTPException: " + (throwable as HttpException).code())
        }
        if (throwable is IOException) {
            // A network or conversion error happened
            Log.e("RepoApiServiceError", "IOException: " + throwable.message)
        }
    }
}
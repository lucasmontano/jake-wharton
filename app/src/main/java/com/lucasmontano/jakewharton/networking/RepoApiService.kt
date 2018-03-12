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

class RepoApiService(private val realm: Realm) {

    private var repoAPI: RepoAPI = RetrofitAdapterFactory.adapter.create<RepoAPI>(RepoAPI::class.java)

    fun getRepo(url: String) : Observable<Response<ResponseData>> {
        return repoAPI.getRepo(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t -> onError(t) }
                .doOnNext { r -> r.body()?.repos?.let { processResponse(it) } }
    }

    private fun processResponse(response: List<RepoData>) {
        realm.beginTransaction()
        response.forEach {

            realm.copyToRealmOrUpdate(it)
        }
        realm.commitTransaction()
    }

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
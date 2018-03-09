package com.lucasmontano.jakewharton.presenter

import com.lucasmontano.jakewharton.BuildConfig
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.view.RepoListView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject

class RepoListPresenter @Inject constructor(private val getRepoInteractor: GetRepoInteractor) : BaseNetworkingPresenter {

    private lateinit var view: RepoListView
    private var repoDisposable: Disposable? = null
    private var pageToBeRequest: String = BuildConfig.JAKE_URL
    var pagesLinks: String? = null

    fun init(view: RepoListView) {
        this.view = view
    }

    override fun unsubscribeFromNetworkRequests() {
        repoDisposable?.run { dispose() }
    }

    override fun subscribeForNetworkRequests() {
        val observer = object: Observer<Result<List<RepoData>>> {

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                repoDisposable = d
            }

            override fun onNext(result: Result<List<RepoData>>) {

                result.response()?.let { response ->

                    response.headers().let { headers ->
                        pagesLinks = headers.get("Link")
                    }

                    response.body()?.let { view.showRepos(it) }
                }
            }

            override fun onError(e: Throwable) = Unit
        }
        getRepoInteractor.observe(observer = observer)
    }

    fun loadRepos() {
        getRepoInteractor.execute(pageToBeRequest)
    }
}
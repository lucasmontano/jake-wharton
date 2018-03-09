package com.lucasmontano.jakewharton.presenter

import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.view.RepoListView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RepoListPresenter @Inject constructor(private val getRepoInteractor: GetRepoInteractor) : BaseNetworkingPresenter {

    private lateinit var view: RepoListView
    private var repoDisposable: Disposable? = null
    private var pageToBeRequest: Int = 1

    fun init(view: RepoListView) {
        this.view = view
    }

    override fun unsubscribeFromNetworkRequests() {
        repoDisposable?.run { dispose() }
    }

    override fun subscribeForNetworkRequests() {
        val observer = object: Observer<List<RepoData>> {

            override fun onComplete() { pageToBeRequest++ }

            override fun onSubscribe(d: Disposable) { repoDisposable = d }

            override fun onNext(dataSet: List<RepoData>) = view.showRepos(dataSet)

            override fun onError(e: Throwable) = Unit
        }
        getRepoInteractor.observe(observer = observer)
    }

    fun loadRepos() {
        getRepoInteractor.execute(pageToBeRequest)
    }
}
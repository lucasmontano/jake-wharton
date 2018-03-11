package com.lucasmontano.jakewharton.presenter

import com.lucasmontano.jakewharton.data.ErrorData
import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.view.interfaces.RepoListView
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class RepoListPresenter @Inject constructor(private val getRepoInteractor: GetRepoInteractor) : BaseNetworkingPresenter {

    private lateinit var view: RepoListView
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun init(view: RepoListView) {
        this.view = view
        getRepoInteractor.observe(getObserver())
    }

    override fun unsubscribeFromNetworkRequests() {
        compositeDisposable.run { dispose() }
    }

    fun loadFirst() {

        // Show top loading.
        view.showTopLoading()

        // Request first page.
        getRepoInteractor.getFirstPage()
    }

    fun loadNext() {

        val hasNextPage : Boolean = getRepoInteractor.getNextPage()

        // Show loading if we have next page.
        if (hasNextPage) {
            view.showNextPageLoading()
        } else {
            view.warnLastPage()
        }
    }

    private fun getObserver(): Observer<ResponseData> {

        return object: Observer<ResponseData> {

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onNext(result: ResponseData) {

                view.hideNextPageLoading()
                view.hideTopLoading()

                result.repos?.let {
                    view.showRepos(it)
                }

                result.error?.let {
                    view.showError(it)
                }
            }

            override fun onError(e: Throwable) {
                view.showError(ErrorData("", ""))
            }
        }
    }
}
package com.lucasmontano.jakewharton.presenter

import com.lucasmontano.jakewharton.data.LinkHeaderData
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.view.RepoListView
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject


class RepoListPresenter @Inject constructor(private val getRepoInteractor: GetRepoInteractor) : BaseNetworkingPresenter {

    private lateinit var view: RepoListView
    private var pagesLinks: LinkHeaderData = LinkHeaderData("")
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun init(view: RepoListView) {
        this.view = view
    }

    override fun unsubscribeFromNetworkRequests() {
        compositeDisposable.run { dispose() }
    }

    fun loadNext() {

        pagesLinks.next?.let {

            // Show loading if we have next page.
            view.showNextPageLoading()

            // Request next page.
            getRepoInteractor.getRepo(it, getObserver())
        }
    }

    fun loadFirst() {

        // Show top loading.
        view.showTopLoading()

        // Request first page.
        getRepoInteractor.getRepo(pagesLinks.first, getObserver())
    }

    private fun getObserver(): Observer<Result<List<RepoData>>> {

        return object: Observer<Result<List<RepoData>>> {

            override fun onComplete() {
                view.hideNextPageLoading()
                view.hideTopLoading()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onNext(result: Result<List<RepoData>>) {

                result.response()?.let { response ->

                    response.headers().let { headers ->

                        headers.get("Link")?.let { pagesLinks = LinkHeaderData(it) }

                        if (pagesLinks.next.isNullOrEmpty()) {
                            view.warnLastPage()
                        }
                    }

                    response.body()?.let { view.showRepos(it) }
                }
            }

            override fun onError(e: Throwable) = Unit
        }
    }
}
package com.lucasmontano.jakewharton.presenter

import com.lucasmontano.jakewharton.data.ErrorData
import com.lucasmontano.jakewharton.data.LinkHeaderData
import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.view.RepoListView
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Response
import javax.inject.Inject


class RepoListPresenter @Inject constructor(private val getRepoInteractor: GetRepoInteractor) : BaseNetworkingPresenter {

    private lateinit var view: RepoListView
    private var pagesLinks: LinkHeaderData = LinkHeaderData("")
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getPagesLinks(): LinkHeaderData {return pagesLinks}

    fun init(view: RepoListView) {
        this.view = view
    }

    override fun unsubscribeFromNetworkRequests() {
        compositeDisposable.run { dispose() }
    }

    fun loadFirst() {

        // Show top loading.
        view.showTopLoading()

        // Request first page.
        getRepoInteractor.getRepo(getPagesLinks().first, getObserver())
    }

    fun loadNext() {

        getPagesLinks().next?.let {

            // Show loading if we have next page.
            view.showNextPageLoading()

            // Request next page.
            getRepoInteractor.getRepo(it, getObserver())
        }
    }

    private fun getObserver(): Observer<Response<ResponseData>> {

        return object: Observer<Response<ResponseData>> {

            override fun onComplete() {
                view.hideNextPageLoading()
                view.hideTopLoading()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onNext(result: Response<ResponseData>) {

                result.body()?.repos?.let {
                    view.showRepos(it)
                }

                result.body()?.error?.let {
                    view.showError(it)
                }

                result.headers().let {

                    it.get("Link")?.let { pagesLinks = LinkHeaderData(it) }

                    if (pagesLinks.next.isNullOrEmpty()) {
                        view.warnLastPage()
                    }
                }
            }

            override fun onError(e: Throwable) {
                view.showError(ErrorData("", ""))
            }
        }
    }
}
package com.lucasmontano.jakewharton.interactor

import com.lucasmontano.jakewharton.BuildConfig
import com.lucasmontano.jakewharton.data.LinkHeaderData
import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.networking.RepoApiService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepoInteractor @Inject constructor(private val repoApiService: RepoApiService) {

    private val subscription: PublishSubject<ResponseData> = PublishSubject.create()
    private var pagesLinks: LinkHeaderData = LinkHeaderData("")

    /**
     * Observe is added to Interactor PublishSubject.
     *
     * @param observer The Observer.
     */
    fun observe(observer: Observer<ResponseData>) {
        return subscription
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    /**
     * Execute the request to get a page of Jake's Repos.
     *
     * @param page The page link.
     */
    private fun execute(page: String) {
        repoApiService.getRepo(page).subscribe(GetRepoObserver())
    }

    /**
     * Request the first page.
     */
    fun getFirstPage() {
        execute(BuildConfig.JAKE_URL)
    }

    /**
     * Request the next page.
     *
     * @return Boolean has next page
     */
    fun getNextPage() : Boolean {
        pagesLinks.next?.let {
            execute(it)
            return true
        }
        return false
    }

    inner class GetRepoObserver : Observer<Response<ResponseData>> {

        override fun onComplete() {

            if (subscription.hasObservers()) {
                if (pagesLinks.next.isNullOrEmpty()) {
                    subscription.onComplete()
                }
            }
        }

        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(result: Response<ResponseData>) {

            if (subscription.hasObservers()) {

                result.headers().let {
                    it.get("Link")?.let { pagesLinks = LinkHeaderData(it) }
                }

                result.body()?.let { subscription.onNext(it) }
            }
        }

        override fun onError(e: Throwable) {

            if (subscription.hasObservers()) {
                subscription.onError(e)
            }
        }
    }
}
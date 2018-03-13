package com.lucasmontano.jakewharton.presenter

import android.net.ConnectivityManager
import com.lucasmontano.jakewharton.data.ErrorData
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.view.interfaces.RepoListView
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RepoListPresenter @Inject constructor(
    private val getRepoInteractor: GetRepoInteractor,
    private val connectivityManager: ConnectivityManager?): BaseNetworkingPresenter {

  private var dataSet: ArrayList<RepoData> = ArrayList()
  private lateinit var view: RepoListView
  private var compositeDisposable: CompositeDisposable = CompositeDisposable()

  /**
   * A view that use this presenter should implement the interface RepoListView.
   */
  fun init(view: RepoListView) {
    this.view = view
    getRepoInteractor.observe(getObserver())

    checkConnectivity()
  }

  /**
   * Check device connectivity.
   *
   * Warn RepoListView if there is no connectivity.
   */
  fun checkConnectivity() {
    connectivityManager?.run {
      val activeNetwork = activeNetworkInfo
      val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
      if ( ! isConnected) view.showError(ErrorData("Check your connectivity ;)", null))
    }
  }

  /**
   * Dispose all the Disposables in CompositeDisposable.
   */
  override fun unsubscribeFromNetworkRequests() {
    compositeDisposable.run { dispose() }
  }

  /**
   * Load the first page and warn the view to show the loading.
   * The DataSet is cleared when the first page is requested.
   */
  fun loadFirst() {

    // Show top loading.
    view.showTopLoading()

    // Request first page.
    getRepoInteractor.getFirstPage()

    // Clear current dataSet.
    dataSet.clear()
  }

  /**
   * Load the next page if any.
   * Warn the view when we are in the last page.
   */
  fun loadNext() {

    val hasNextPage: Boolean = getRepoInteractor.getNextPage()

    // Show loading if we have next page.
    if (hasNextPage) {
      view.showNextPageLoading()
    } else {
      view.warnLastPage()
    }
  }

  /**
   * Observe the Interactor which will Publish the dataSet and errors.
   *
   * The dataSet will be updated merging the new page with the last ones. The view only have to
   * update the adapter dataSet or display the error. In some case we will have dataSet and error.
   */
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
          dataSet.addAll(it)
          view.showRepos(dataSet)
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
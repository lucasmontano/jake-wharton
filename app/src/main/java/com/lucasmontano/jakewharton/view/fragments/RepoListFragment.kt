package com.lucasmontano.jakewharton.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucasmontano.jakewharton.JakeApp

import com.lucasmontano.jakewharton.R
import com.lucasmontano.jakewharton.data.ErrorData
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.presenter.RepoListPresenter
import com.lucasmontano.jakewharton.view.adapters.RepoRecyclerViewAdapter
import com.lucasmontano.jakewharton.view.interfaces.RepoListView
import kotlinx.android.synthetic.main.fragment_repo_list.view.*
import javax.inject.Inject
import android.content.Intent
import android.net.Uri


class RepoListFragment: Fragment(), RepoListView {

  @Inject
  lateinit var presenter: RepoListPresenter

  private var mListener: OnListFragmentInteractionListener? = null
  private val adapter = RepoRecyclerViewAdapter(mListener)
  private lateinit var layoutManager: LinearLayoutManager

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    (activity!!.applicationContext as JakeApp).component.inject(this)

    presenter.init(this)
    presenter.loadFirst()
  }

  /**
   * Inflate and start views.
   */
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_repo_list, container, false)

    initRecyclerView(view)
    initSwipeRefresh(view)

    return view
  }

  /**
   * Setup the SwipeRefresh.
   *
   * - On Pull if it is not loading new repos, request the first page.
   * - On pull if it is loading new repos, set swipe refreshing status to false.
   */
  private fun initSwipeRefresh(view: View) {
    view.swipeRefreshLayout_repos.setOnRefreshListener {

      if (!adapter.isLoading) presenter.loadFirst() else view.swipeRefreshLayout_repos.isRefreshing = false
    }
  }

  /**
   * Setup the RecyclerView.
   *
   * - onScrolled: check the threshold with last visible item to request next page.
   * - Enable swipeRefresh only at top.
   */
  private fun initRecyclerView(view: View) {
    layoutManager = LinearLayoutManager(view.context)
    view.recyclerView_repos.layoutManager = layoutManager
    view.recyclerView_repos.adapter = adapter
    view.recyclerView_repos.addOnScrollListener(object: RecyclerView.OnScrollListener() {

      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        // Load next page.
        if (!adapter.isLoading && totalItemCount <= (lastVisibleItem + adapter.visibleThreshold)) {
          presenter.loadNext()
        }

        // Enable swipeRefresh only at top.
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        view.swipeRefreshLayout_repos.isEnabled = firstVisiblePosition == 0
      }
    })
  }

  /**
   * The activity must implement OnListFragmentInteractionListener.
   */
  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is OnListFragmentInteractionListener) {
      mListener = context
    } else {
      throw RuntimeException(
          context!!.toString() + " must implement OnListFragmentInteractionListener")
    }
  }

  /**
   * onDetach call presenter to dispose the disposables.
   */
  override fun onDetach() {
    super.onDetach()
    mListener = null
    presenter.unsubscribeFromNetworkRequests()
  }

  /**
   * View behavior: display list of repos.
   */
  override fun showRepos(dataSet: List<RepoData>) {

    adapter.run {
      adapter.dataSet = dataSet
      notifyDataSetChanged()
    }
  }

  /**
   * View behavior: disable swipe refresh and show loading below repo list.
   */
  override fun showNextPageLoading() {

    view?.swipeRefreshLayout_repos?.isEnabled = false

    adapter.run {
      isLoading = true
      notifyDataSetChanged()
    }
  }

  /**
   * View behavior: hide loading below repo list.
   */
  override fun hideNextPageLoading() {
    adapter.run {
      isLoading = false
      notifyDataSetChanged()
    }
  }

  /**
   * View behavior: overScroll disabled if would have is next pages,
   * change swipe refreshing status to false.
   */
  override fun showTopLoading() {
    view?.swipeRefreshLayout_repos?.isRefreshing = true
    view?.recyclerView_repos?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
  }

  /**
   * View behavior: change swipe refreshing status to false.
   */
  override fun hideTopLoading() {
    view?.swipeRefreshLayout_repos?.isRefreshing = false
  }

  /**
   * View behavior: overScroll when is the last page.
   */
  override fun warnLastPage() {
    view?.recyclerView_repos?.overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
  }

  /**
   * Show error message and link to documentation (if any link).
   *
   * @param errorData contains the message and the documentation_url.
   */
  override fun showError(errorData: ErrorData) {

    view?.let { view ->

      errorData.message?.let { message ->

        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)

        errorData.documentation_url?.let { url ->

          snackBar.setAction(R.string.detail) {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
          }
        }

        snackBar.show()
      }
    }
  }

  interface OnListFragmentInteractionListener {
    fun onListFragmentInteraction(repo: RepoData)
  }

  companion object {

    fun newInstance(): RepoListFragment {
      return RepoListFragment()
    }
  }
}

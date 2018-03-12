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


class RepoListFragment : Fragment(), RepoListView {

    @Inject lateinit var presenter: RepoListPresenter

    private var mListener: OnListFragmentInteractionListener? = null
    private val adapter = RepoRecyclerViewAdapter(mListener)
    private lateinit var layoutManager: LinearLayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity!!.applicationContext as JakeApp).component.inject(this)

        presenter.init(this)
        presenter.loadFirst()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_repo_list, container, false)

        layoutManager = LinearLayoutManager(view.context)
        view.recyclerView_repos.layoutManager = layoutManager
        view.recyclerView_repos.adapter = adapter
        view.recyclerView_repos.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                // Load next page.
                if ( ! adapter.isLoading && totalItemCount <= (lastVisibleItem + adapter.visibleThreshold)) {
                    presenter.loadNext()
                }

                // Enable swipeRefresh only at top.
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                view.swipeRefreshLayout_repos.isEnabled = firstVisiblePosition == 0
            }
        })

        view.swipeRefreshLayout_repos.setOnRefreshListener {

            if ( ! adapter.isLoading) presenter.loadFirst() else view.swipeRefreshLayout_repos.isRefreshing = false
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
        presenter.unsubscribeFromNetworkRequests()
    }

    override fun showRepos(dataSet: List<RepoData>) {

        adapter.run {
            adapter.dataSet = dataSet
            notifyDataSetChanged()
        }
    }

    override fun showNextPageLoading() {

        view?.swipeRefreshLayout_repos?.isEnabled = false

        adapter.run {
            isLoading = true
            notifyDataSetChanged()
        }
    }

    override fun hideNextPageLoading() {

        view?.swipeRefreshLayout_repos?.isEnabled = true

        adapter.run {
            isLoading = false
            notifyDataSetChanged()
        }
    }

    override fun showTopLoading() {
        view?.swipeRefreshLayout_repos?.isRefreshing = true

        // overScroll disabled if would have is next pages.
        view?.recyclerView_repos?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    override fun hideTopLoading() {
        view?.swipeRefreshLayout_repos?.isRefreshing = false
    }

    override fun warnLastPage() {

        // overScroll when is the last page.
        view?.recyclerView_repos?.overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
    }

    override fun showError(e: ErrorData) {

        // Show error message and link to documentation (if any link)
        view?.let { view ->

            e.message?.let { message ->

                val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)

                e.documentation_url?.let { url ->

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
        fun onListFragmentInteraction(item: RepoData)
    }

    companion object {

        fun newInstance(): RepoListFragment {
            return RepoListFragment()
        }
    }
}

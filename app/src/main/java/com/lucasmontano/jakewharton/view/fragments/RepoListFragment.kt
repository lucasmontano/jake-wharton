package com.lucasmontano.jakewharton.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import javax.inject.Inject

class RepoListFragment : Fragment(), RepoListView {

    @Inject lateinit var presenter: RepoListPresenter

    private var mListener: OnListFragmentInteractionListener? = null
    private val adapter = RepoRecyclerViewAdapter(mListener)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity!!.applicationContext as JakeApp).component.inject(this)

        presenter.init(this)
        presenter.loadFirst()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_repo_list, container, false)

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(view.getContext())
            view.adapter = adapter
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
        adapter.setDataSet(dataSet)
        adapter.notifyDataSetChanged()
    }

    override fun showNextPageLoading() {

    }

    override fun hideNextPageLoading() {

    }

    override fun showTopLoading() {
        Log.d("Loading", "Show")
    }

    override fun hideTopLoading() {
        Log.d("Loading", "Hide")
    }

    override fun warnLastPage() {

    }

    override fun showError(e: ErrorData) {

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

package com.lucasmontano.jakewharton.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.lucasmontano.jakewharton.R
import com.lucasmontano.jakewharton.data.RepoData

import com.lucasmontano.jakewharton.view.fragments.RepoListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_repos.view.*
import kotlinx.android.synthetic.main.repo_loading.view.*

class RepoRecyclerViewAdapter(private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private object ViewTypes {
        const val Repo = 1
        const val LoadingMore = 2
    }

    internal var dataSet: List<RepoData>? = null
    internal var isLoading: Boolean = false
    internal val visibleThreshold: Int = 3

    override fun getItemCount(): Int {

        if (dataSet == null) return 0

        // Add +1 for Loading.
        return if (isLoading) dataSet!!.size + 1 else dataSet!!.size
    }

    override fun getItemViewType(position : Int) : Int {

        // Show loading as last RecyclerView item, If loading.
        return if (isLoading && position == itemCount - 1) ViewTypes.LoadingMore else ViewTypes.Repo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ViewTypes.LoadingMore -> inflateLoadingView(parent)
            else -> inflateRepoView(parent)
        }
    }

    private fun inflateRepoView(parent: ViewGroup): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_repos, parent, false)
        return RepoViewHolder(view)
    }

    private fun inflateLoadingView(parent: ViewGroup): LoadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_loading, parent, false)
        return LoadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            ViewTypes.LoadingMore -> bindLoading(holder as LoadingViewHolder)
            else -> bindRepo(holder as RepoViewHolder, position)
        }
    }

    private fun bindLoading(holder: LoadingViewHolder) {
        holder.mLoading.visibility = View.VISIBLE
    }

    private fun bindRepo(holder: RepoViewHolder, position: Int) {

        holder.mItem = dataSet!![position]
        holder.mName.text = dataSet!![position].name

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
    }

    inner class RepoViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mName: TextView = mView.textView_name as TextView
        var mItem: RepoData? = null
    }

    inner class LoadingViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {

        val mLoading: ProgressBar = mView.progressBar_loading as ProgressBar
    }
}

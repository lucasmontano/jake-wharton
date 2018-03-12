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
import kotlinx.android.synthetic.main.repo_item.view.*
import kotlinx.android.synthetic.main.repo_loading.view.*

class RepoRecyclerViewAdapter(
    private val mListener: OnListFragmentInteractionListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  /**
   * View Types
   */
  private object ViewTypes {
    const val Repo = 1
    const val LoadingMore = 2
  }

  internal var dataSet: List<RepoData>? = null
  internal var isLoading: Boolean = false
  internal val visibleThreshold: Int = 3

  /**
   * Add +1 for Loading when isLoading = TRUE.
   */
  override fun getItemCount(): Int {

    if (dataSet == null) return 0

    return if (isLoading) dataSet!!.size + 1 else dataSet!!.size
  }

  /**
   * Show loading as last RecyclerView item, If loading.
   *
   * If adapter has a loading status and is at the bottom: Show Loading
   *
   * @param position
   */
  override fun getItemViewType(position: Int): Int {
    return if (isLoading && position == itemCount - 1) ViewTypes.LoadingMore else ViewTypes.Repo
  }

  /**
   * @param parent
   * @param viewType
   */
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    return when (viewType) {
      ViewTypes.LoadingMore -> inflateLoadingView(parent)
      else -> inflateRepoView(parent)
    }
  }

  /**
   * Inflate the Repo View.
   *
   * @param parent
   */
  private fun inflateRepoView(parent: ViewGroup): RepoViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
    return RepoViewHolder(view)
  }

  /**
   * Inflate the Loading View, used when is loading next page.
   *
   * @param parent
   */
  private fun inflateLoadingView(parent: ViewGroup): LoadingViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_loading, parent, false)
    return LoadingViewHolder(view)
  }

  /**
   * @param holder
   * @param position
   */
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    when (holder.itemViewType) {
      ViewTypes.LoadingMore -> bindLoading(holder as LoadingViewHolder)
      else -> bindRepo(holder as RepoViewHolder, position)
    }
  }

  /**
   * BindViewHolder for Loading ViewType.
   *
   * @param holder
   */
  private fun bindLoading(holder: LoadingViewHolder) {
    holder.mLoading.visibility = View.VISIBLE
  }

  /**
   * BindViewHolder for Repo ViewType.
   *
   * @param holder
   * @param position
   */
  private fun bindRepo(holder: RepoViewHolder, position: Int) {

    holder.mItem = dataSet!![position]
    holder.mName.text = dataSet!![position].name

    holder.mView.setOnClickListener {
      mListener?.onListFragmentInteraction(holder.mItem!!)
    }
  }

  /**
   * Repo ViewHolder.
   */
  inner class RepoViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {

    val mName: TextView = mView.textView_name as TextView
    var mItem: RepoData? = null
  }

  /**
   * Loading ViewHolder.
   */
  inner class LoadingViewHolder(mView: View): RecyclerView.ViewHolder(mView) {

    val mLoading: ProgressBar = mView.progressBar_loading as ProgressBar
  }
}

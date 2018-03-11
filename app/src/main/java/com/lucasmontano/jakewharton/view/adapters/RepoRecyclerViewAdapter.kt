package com.lucasmontano.jakewharton.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lucasmontano.jakewharton.R
import com.lucasmontano.jakewharton.data.RepoData

import com.lucasmontano.jakewharton.view.fragments.RepoListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_repos.view.*

class RepoRecyclerViewAdapter(private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<RepoRecyclerViewAdapter.ViewHolder>() {

    private var mValues: List<RepoData>? = null

    override fun getItemCount(): Int {
        return mValues?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_repos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues!![position]
        holder.mName.text = mValues!![position].name

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mName: TextView = mView.textView_name as TextView
        var mItem: RepoData? = null
    }

    fun setDataSet(dataSet: List<RepoData>) {
        mValues = dataSet
    }
}

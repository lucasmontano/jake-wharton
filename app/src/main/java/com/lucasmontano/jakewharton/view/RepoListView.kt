package com.lucasmontano.jakewharton.view

import com.lucasmontano.jakewharton.data.RepoData

interface RepoListView {

    /**
     * Show Jake' Repos :)
     *
     * @param dataSet The data set to show.
     */
    fun showRepos(dataSet: List<RepoData>)
}
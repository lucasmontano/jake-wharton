package com.lucasmontano.jakewharton.view

import com.lucasmontano.jakewharton.data.RepoData

interface RepoListView {

    /**
     * Show Jake' Repos :)
     *
     * @param dataSet The data set to show.
     */
    fun showRepos(dataSet: List<RepoData>)

    /**
     * Show loading for next page request.
     */
    fun showNextPageLoading()

    /**
     * Hide loading for next page.
     */
    fun hideNextPageLoading()

    /**
     * Show loading for first page request.
     */
    fun showTopLoading()

    /**
     * Hide loading for first page request.
     */
    fun hideTopLoading()

    /**
     * Warn view that reached last page.
     */
    fun warnLastPage()

    /**
     * Wand view about an error.
     */
    fun showError(e: Throwable)
}
package com.lucasmontano.jakewharton.presenter

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.interactor.GetRepoInteractor
import com.lucasmontano.jakewharton.networking.RepoApiService
import com.lucasmontano.jakewharton.networking.RestAdapterFactory
import com.lucasmontano.jakewharton.view.RepoListView
import org.junit.ClassRule
import java.util.concurrent.CompletableFuture

@RunWith(MockitoJUnitRunner::class)
class RepoListPresenterUnitTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var repoApiService : RepoApiService
    private lateinit var repoListPresenter: RepoListPresenter

    @Before
    fun setUp() {
        repoApiService = RepoApiService(RestAdapterFactory.adapter)
        repoListPresenter = RepoListPresenter(GetRepoInteractor(repoApiService))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadRepos() {

        val future = CompletableFuture<List<RepoData>>()

        val repoListView : RepoListView = object : RepoListView {

            override fun showTopLoading() {

            }

            override fun hideTopLoading() {

            }

            override fun showNextPageLoading() {

            }

            override fun hideNextPageLoading() {

            }

            override fun showRepos(dataSet: List<RepoData>) {
                future.complete(dataSet)
            }

            override fun warnLastPage() {

            }
        }
        repoListPresenter.init(repoListView)
        repoListPresenter.loadFirst()

        future.get().let {
            Assert.assertTrue(it.isNotEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun testPaginationLimits() {

        val future = CompletableFuture<Int>()
        var pages = 0

        val repoListView : RepoListView = object : RepoListView {

            override fun hideTopLoading() {

            }

            override fun warnLastPage() {
                future.complete(pages)
            }

            override fun showTopLoading() {

            }

            override fun showNextPageLoading() {

            }

            override fun hideNextPageLoading() {
                repoListPresenter.loadNext()
            }

            override fun showRepos(dataSet: List<RepoData>) {
                pages += 1
            }
        }
        repoListPresenter.init(repoListView)
        repoListPresenter.loadFirst()

        future.get().let {
            Assert.assertTrue(it == 6)
        }
    }

    @After
    fun tearDown() {
        repoListPresenter.unsubscribeFromNetworkRequests()
    }
}
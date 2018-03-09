package com.lucasmontano.jakewharton.networking

import io.reactivex.Observable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.RepoData
import org.junit.ClassRule

@RunWith(MockitoJUnitRunner::class)
class RepoApiServiceUnitTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var repoApiService : RepoApiService
    private lateinit var request: RepoRequest

    @Before
    fun setUp() {
        repoApiService = RepoApiService(RestAdapterFactory.adapter)
        request = RepoRequest(1, 15)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRepo() {
        val observable : Observable<List<RepoData>> = repoApiService.getRepo(request)
        observable.subscribe { t -> t.let { Assert.assertTrue(it.isNotEmpty()) } }
    }

    @After
    fun tearDown() {

    }
}
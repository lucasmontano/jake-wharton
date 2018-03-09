package com.lucasmontano.jakewharton.networking

import com.lucasmontano.jakewharton.BuildConfig
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.RepoData
import org.junit.ClassRule
import retrofit2.adapter.rxjava2.Result

@RunWith(MockitoJUnitRunner::class)
class RepoApiServiceUnitTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var repoApiService : RepoApiService

    @Before
    fun setUp() {
        repoApiService = RepoApiService(RestAdapterFactory.adapter)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRepo() {
        val observable : Observable<Result<List<RepoData>>> = repoApiService.getRepo(BuildConfig.JAKE_URL)
        observable.blockingFirst().response()?.body()?.isNotEmpty()?.let { Assert.assertTrue(it) }
    }

    @After
    fun tearDown() {

    }
}
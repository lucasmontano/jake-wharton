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
import com.lucasmontano.jakewharton.data.ResponseData
import org.junit.ClassRule
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RepoApiServiceUnitTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var repoApiService : RepoApiService

    @Before
    fun setUp() {
        repoApiService = RepoApiService(RetrofitAdapterFactory.adapter)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRepo() {
        val observable : Observable<Response<ResponseData>> = repoApiService.getRepo(BuildConfig.JAKE_URL)
        observable.blockingFirst().body()?.repos?.isNotEmpty()?.let { Assert.assertTrue(it) }
    }

    @After
    fun tearDown() {

    }
}
package com.lucasmontano.jakewharton.networking

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RepoApiServiceUnitTest {

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
        val observable : Observable<Response<RepoResponse>> = repoApiService.getRepo(request)
        observable.subscribe(Consumer { t -> Assert.assertNotNull(t.body()) })
    }

    @After
    fun tearDown() {

    }
}
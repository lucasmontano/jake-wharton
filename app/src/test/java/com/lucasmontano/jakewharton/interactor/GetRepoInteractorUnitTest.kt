package com.lucasmontano.jakewharton.interactor

import com.lucasmontano.jakewharton.BuildConfig
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.networking.RepoApiService
import com.lucasmontano.jakewharton.networking.RetrofitAdapterFactory
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.junit.*
import retrofit2.Response
import java.util.concurrent.CompletableFuture

@RunWith(MockitoJUnitRunner::class)
class GetRepoInteractorUnitTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var getRepoInteractor : GetRepoInteractor

    @Before
    fun setUp() {
        val repoApiService = RepoApiService(RetrofitAdapterFactory.adapter)
        getRepoInteractor = GetRepoInteractor(repoApiService = repoApiService)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRepoInteractor() {

        val future = CompletableFuture<Response<ResponseData>>()

        val observer = object: Observer<Response<ResponseData>> {

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseData>) {
                future.complete(t)
            }

            override fun onError(e: Throwable) {

            }
        }

        getRepoInteractor.getRepo(BuildConfig.JAKE_URL, observer)

        future.get().body()?.repos?.forEach { repoData: RepoData -> Assert.assertNotNull(repoData.description) }
    }

    @After
    fun tearDown() {

    }
}
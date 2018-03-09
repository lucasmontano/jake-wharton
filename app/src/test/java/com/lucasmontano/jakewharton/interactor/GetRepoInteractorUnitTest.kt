package com.lucasmontano.jakewharton.interactor

import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.networking.RepoApiService
import com.lucasmontano.jakewharton.networking.RestAdapterFactory
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.junit.*
import retrofit2.adapter.rxjava2.Result
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
        val repoApiService = RepoApiService(RestAdapterFactory.adapter)
        getRepoInteractor = GetRepoInteractor(repoApiService = repoApiService)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRepoInteractor() {

        val future = CompletableFuture<Result<List<RepoData>>>()

        val observer = object: Observer<Result<List<RepoData>>> {

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Result<List<RepoData>>) {
                future.complete(t)
            }

            override fun onError(e: Throwable) {

            }
        }

        getRepoInteractor.observe(observer = observer)
        getRepoInteractor.execute(1)

        future.get().response()?.body()?.forEach { repoData: RepoData -> Assert.assertNotNull(repoData.description) }
    }

    @After
    fun tearDown() {

    }
}
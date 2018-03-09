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

@RunWith(MockitoJUnitRunner::class)
class GetRepoInteractorUnitTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var getRepoInteractor : GetRepoInteractor

    @Before
    fun setUp() {
        var repoApiService = RepoApiService(RestAdapterFactory.adapter)
        getRepoInteractor = GetRepoInteractor(repoApiService = repoApiService)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRepoInteractor() {

        val observer = object: Observer<List<RepoData>> {

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<RepoData>) {
                t.forEach { repoData: RepoData -> Assert.assertNotNull(repoData.description) }
            }

            override fun onError(e: Throwable) {

            }
        }

        getRepoInteractor.observe(observer = observer)
        getRepoInteractor.execute(1)
    }

    @After
    fun tearDown() {

    }
}
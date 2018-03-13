package com.lucasmontano.jakewharton.interactor

import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.data.ResponseData
import com.lucasmontano.jakewharton.networking.RepoApiService
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.junit.*
import java.util.concurrent.CompletableFuture

@RunWith(MockitoJUnitRunner::class)
class GetRepoInteractorUnitTest {

  companion object {
    @ClassRule
    @JvmField
    val schedulers = RxImmediateSchedulerRule()
  }

  private lateinit var getRepoInteractor: GetRepoInteractor

  @Before
  fun setUp() {
    val repoApiService = RepoApiService(null)
    getRepoInteractor = GetRepoInteractor(repoApiService = repoApiService, realm = null)
  }

  @Test
  @Throws(Exception::class)
  fun testGetRepoInteractor() {

    val future = CompletableFuture<ResponseData>()

    val observer = object: Observer<ResponseData> {

      override fun onComplete() {

      }

      override fun onSubscribe(d: Disposable) {

      }

      override fun onNext(t: ResponseData) {
        future.complete(t)
      }

      override fun onError(e: Throwable) {

      }
    }

    getRepoInteractor.observe(observer)
    getRepoInteractor.getFirstPage()

    future.get()?.repos?.forEach { repoData: RepoData ->
      Assert.assertNotNull(repoData.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun testErrorReturned() {

    val future = CompletableFuture<String>()

    val observer = object: Observer<ResponseData> {

      override fun onComplete() {

      }

      override fun onSubscribe(d: Disposable) {

      }

      override fun onNext(t: ResponseData) {
        future.complete(t.error?.message)
      }

      override fun onError(e: Throwable) {

      }
    }

    getRepoInteractor.setGitHubUserBaseUrl(
        "https://api.github.com/user/9999999999/repos?page=1&per_page=15")
    getRepoInteractor.observe(observer)
    getRepoInteractor.getFirstPage()

    future.get().let {
      Assert.assertEquals("Error Not Found expected but receive $it", "Not Found", it)
    }
  }

  @After
  fun tearDown() {

  }
}
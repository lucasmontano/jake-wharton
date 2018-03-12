package com.lucasmontano.jakewharton.networking

import com.lucasmontano.jakewharton.BuildConfig
import com.lucasmontano.jakewharton.MockSupport
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.lucasmontano.jakewharton.RxImmediateSchedulerRule
import com.lucasmontano.jakewharton.data.ResponseData
import io.realm.Realm
import org.junit.ClassRule
import org.junit.Rule
import org.powermock.core.classloader.annotations.PowerMockIgnore
import retrofit2.Response
import org.powermock.modules.junit4.rule.PowerMockRule



@RunWith(MockitoJUnitRunner::class)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
class RepoApiServiceUnitTest {

  companion object {
    @ClassRule
    @JvmField
    val schedulers = RxImmediateSchedulerRule()
  }

  private lateinit var repoApiService: RepoApiService

  private var mockRealm: Realm? = null

  @Rule
  var rule = PowerMockRule()

  @Before
  fun setUp() {
    mockRealm = MockSupport.mockRealm()
    repoApiService = RepoApiService(mockRealm!!)
  }

  @Test
  @Throws(Exception::class)
  fun testGetRepo() {
    val observable: Observable<Response<ResponseData>> = repoApiService.getRepo(BuildConfig.JAKE_URL)
    observable.blockingFirst().body()?.repos?.isNotEmpty()?.let { Assert.assertTrue(it) }
  }

  @After
  fun tearDown() {

  }
}
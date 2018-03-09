package com.lucasmontano.jakewharton.interactor

import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.networking.RepoApiService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepoInteractor @Inject constructor(private val repoApiService: RepoApiService) {

    var repoSubscription: AsyncSubject<Result<List<RepoData>>> = AsyncSubject.create()

    /**
     * Get a page of Jake's Repos
     *
     * @param page The page number.
     */
    fun execute(page: String) {
        repoApiService.getRepo(page).subscribe(repoSubscription)
    }

    /**
     * Observes Repos
     *
     * @param observer The Observer of Repos.
     */
    fun observe(observer: Observer<Result<List<RepoData>>>) {
        return repoSubscription
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}
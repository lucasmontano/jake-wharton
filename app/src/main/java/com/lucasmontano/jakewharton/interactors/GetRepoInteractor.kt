package com.lucasmontano.jakewharton.interactors

import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.networking.RepoApiService
import com.lucasmontano.jakewharton.networking.RepoRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepoInteractor @Inject constructor(private val repoApiService: RepoApiService) {

    var repoSubscription: BehaviorSubject<List<RepoData>> = BehaviorSubject.create()

    /**
     * Get Jake Repos
     */
    fun execute(page: Int) {

        val request = RepoRequest(
            page = page,
            per_page = 15
        )
        repoApiService.getRepo(request).subscribe(repoSubscription)
    }

    /**
     * Observe Repos
     *
     * @param observer
     */
    fun observe(observer: Observer<List<RepoData>>) {
        return repoSubscription
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}
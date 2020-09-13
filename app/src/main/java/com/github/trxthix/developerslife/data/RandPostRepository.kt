package com.github.trxthix.developerslife.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.trxthix.developerslife.di.AppSingleton
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AppSingleton
class RandPostRepository @Inject constructor(
    private val api: DevLifeApi,
    private val memoryCache: PostMemoryCache
) {
    private val _hasPreviousPost = MutableLiveData<Boolean>(false)
    val hasPreviousPost: LiveData<Boolean> get() = _hasPreviousPost

    fun currentPost(): Observable<Post> {
        return if (memoryCache.hasCurrent()) {
            Observable.just(memoryCache.current())
        } else {
            nextPost()
        }
    }

    fun previousPost(): Observable<Post> {
        return if (memoryCache.hasPrevious()) {
            Observable.just(memoryCache.previous())
                .doOnNext { _hasPreviousPost.postValue(memoryCache.hasPrevious()) }
        } else {
            Observable.empty()
        }
    }

    fun nextPost(): Observable<Post> {
        return if (memoryCache.hasNext()) {
            Observable.just(memoryCache.next())
                .doOnNext { _hasPreviousPost.postValue(memoryCache.hasPrevious()) }
        } else {
            api.rand()
                .doOnNext {
                    memoryCache.add(it)
                    _hasPreviousPost.postValue(memoryCache.hasPrevious())
                }
                .subscribeOn(Schedulers.io())
        }
    }
}
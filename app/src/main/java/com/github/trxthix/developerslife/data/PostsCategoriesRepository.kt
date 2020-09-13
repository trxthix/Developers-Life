package com.github.trxthix.developerslife.data

import androidx.paging.PagingSource.LoadResult
import com.github.trxthix.developerslife.di.AppSingleton
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Репозиторий для постраничной загрузки постов
 */
@AppSingleton
class PostsCategoriesRepository @Inject constructor(
    private val api: DevLifeApi
) {
    fun getLatest(page: Int): Single<LoadResult<Int, Post>> {
        return api.latest(page)
            .map { toLoadResult(page, it.result) }
            .onErrorReturn(this::toLoadError)
            .subscribeOn(Schedulers.io())
    }

    fun getTop(page: Int): Single<LoadResult<Int, Post>> {
        return api.top(page)
            .map { toLoadResult(page, it.result) }
            .onErrorReturn(this::toLoadError)
            .subscribeOn(Schedulers.io())
    }

    private fun toLoadResult(page: Int, data: List<Post>): LoadResult<Int, Post> {
        return LoadResult.Page(data, getPreviousPageKey(page), getNextPageKey(data, page))
    }

    private fun getNextPageKey(data: List<Post>, currentPage: Int) =
        if (data.isEmpty()) null else currentPage + 1

    private fun getPreviousPageKey(page: Int) = if (page == 0) null else page - 1

    private fun toLoadError(t: Throwable): LoadResult<Int, Post> {
        Timber.e(t)
        return LoadResult.Error(t)
    }
}
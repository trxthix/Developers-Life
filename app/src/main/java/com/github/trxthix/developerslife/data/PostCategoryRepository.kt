package com.github.trxthix.developerslife.data

import androidx.paging.PagingSource.LoadResult
import com.github.trxthix.developerslife.di.AppSingleton
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

private const val PAGE_OFFSET = 1

@AppSingleton
class PostCategoryRepository @Inject constructor(
    private val api: DevLifeApi
) {
    fun getLatest(page: Int): Single<LoadResult<Int, Post>> {
        return api.latestPosts(page)
            .map { toLoadResult(page, it.result) }
            .onErrorReturn(this::toLoadError)
            .subscribeOn(Schedulers.io())
    }

    fun getTop(page: Int): Single<LoadResult<Int, Post>> {
        return api.topPosts(page)
            .map { toLoadResult(page, it.result) }
            .onErrorReturn(this::toLoadError)
            .subscribeOn(Schedulers.io())
    }

    private fun toLoadResult(page: Int, data: List<Post>): LoadResult<Int, Post> =
        LoadResult.Page(data, getPreviousPageKey(page), getNextPageKey(data, page))

    private fun getNextPageKey(data: List<Post>, currentPage: Int) =
        if (data.isEmpty()) null else currentPage + PAGE_OFFSET

    private fun getPreviousPageKey(page: Int) = if (page == 0) null else page - PAGE_OFFSET

    private fun toLoadError(t: Throwable): LoadResult<Int, Post> {
        Timber.e(t)
        return LoadResult.Error(t)
    }
}
package com.github.trxthix.developerslife.data

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single

class PostPagingSource(
    private val postsCategoriesRepository: PostsCategoriesRepository,
    private val category: PostCategory
) : RxPagingSource<Int, Post>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Post>> {
        return when (category) {
            PostCategory.LATEST -> postsCategoriesRepository.getLatest(params.key ?: 0)
            PostCategory.TOP -> postsCategoriesRepository.getTop(params.key ?: 0)
        }
    }
}
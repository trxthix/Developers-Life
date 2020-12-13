package com.github.trxthix.developerslife.data

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single

private const val INITIAL_KEY = 0

class PostCategoryPagingSource(
    private val postCategoryRepository: PostCategoryRepository,
    private val category: PostCategory
) : RxPagingSource<Int, Post>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Post>> {
        return when (category) {
            PostCategory.LATEST -> postCategoryRepository.getLatest(params.key ?: INITIAL_KEY)
            PostCategory.TOP -> postCategoryRepository.getTop(params.key ?: INITIAL_KEY)
        }
    }
}
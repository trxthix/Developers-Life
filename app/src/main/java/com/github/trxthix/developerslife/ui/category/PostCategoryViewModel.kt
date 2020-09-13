package com.github.trxthix.developerslife.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import com.github.trxthix.developerslife.data.Post
import com.github.trxthix.developerslife.data.PostCategory
import com.github.trxthix.developerslife.data.PostPagingSource
import com.github.trxthix.developerslife.data.PostsCategoriesRepository
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostCategoryViewModel @Inject constructor(
    private val postsCategoriesRepository: PostsCategoriesRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private lateinit var pager: Pager<Int, Post>
    private var category: PostCategory? = null

    lateinit var postPagination: Flowable<PagingData<Post>>
        private set

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setCategory(category: PostCategory) {
        if (this.category != category) {
            this.category = category
            initPager(category)
        }
    }

    private fun initPager(category: PostCategory) {
        val pagingConfig = PagingConfig(PAGE_SIZE, PREFETCH_DISTANCE, maxSize = MAX_SIZE)
        pager = Pager(pagingConfig, INITIAL_KEY) {
            /* paging source factory */
            PostPagingSource(postsCategoriesRepository, category)
        }
        postPagination = pager.flowable.cachedIn(viewModelScope)
    }

    private companion object {
        const val PAGE_SIZE = 15
        const val PREFETCH_DISTANCE = 5
        const val INITIAL_KEY = 0
        const val MAX_SIZE = 500
    }
}


package com.github.trxthix.developerslife.ui.category

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.github.trxthix.developerslife.App
import com.github.trxthix.developerslife.R
import com.github.trxthix.developerslife.data.PostCategory
import com.github.trxthix.developerslife.ui.common.MarginItemDecoration
import com.github.trxthix.developerslife.util.attachToLifecycle
import kotlinx.android.synthetic.main.fragment_post_category.*
import kotlinx.android.synthetic.main.include_error_layout.*

private const val KEY_POST_CATEGORY = "com.github.trxthix.developerslife.categories_fragment.post_category"

private const val CHILD_POST_RECYCLER = 0
private const val CHILD_PROGRESS_BAR = 1
private const val CHILD_ERROR_LAYOUT = 2

class PostCategoryFragment : Fragment(R.layout.fragment_post_category) {
    private val viewModel by viewModels<PostCategoryViewModel>{
        App.appComponent.getViewModelFactory()
    }
    private lateinit var pagingAdapter: PagingPostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = arguments?.getSerializable(KEY_POST_CATEGORY) as? PostCategory
            ?: error("PostCategoriesFragment require a PostCategory")
        viewModel.setCategory(category)
        initView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.postPagination
            .subscribe { pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it) }
            .attachToLifecycle(viewLifecycleOwner)
    }

    private fun initView() {
        initRecyclerView()
        btnRetry.setOnClickListener {
            pagingAdapter.refresh()
        }
    }

    private fun initRecyclerView() {
        pagingAdapter = PagingPostAdapter().apply {
            addLoadStateListener {
                when (it.refresh) {
                    is LoadState.NotLoading -> viewAnimator.displayedChild = CHILD_POST_RECYCLER
                    is LoadState.Loading -> viewAnimator.displayedChild = CHILD_PROGRESS_BAR
                    is LoadState.Error -> viewAnimator.displayedChild = CHILD_ERROR_LAYOUT
                }
            }
        }

        val concatAdapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateAdapter(pagingAdapter::retry),
            footer = PagingStateAdapter(pagingAdapter::retry)
        )

        with(postRecycler) {
            adapter = concatAdapter
            addItemDecoration(MarginItemDecoration(getItemOffset()))
        }
    }

    private fun getItemOffset(): Rect {
        val spacing = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_small)
        return Rect(spacing, spacing, spacing, spacing)
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this)
            .resumeRequests()
    }

    override fun onPause() {
        super.onPause()
        Glide.with(this)
            .pauseRequests()
    }

    companion object {
        fun newInstance(category: PostCategory) = PostCategoryFragment().apply {
            arguments = bundleOf(KEY_POST_CATEGORY to category)
        }
    }
}

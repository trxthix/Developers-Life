package com.github.trxthix.developerslife.ui.category

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import com.github.trxthix.developerslife.App
import com.github.trxthix.developerslife.R
import com.github.trxthix.developerslife.data.PostCategory
import com.github.trxthix.developerslife.ui.common.MarginItemDecoration
import com.github.trxthix.developerslife.util.attachToLifecycle
import kotlinx.android.synthetic.main.fragment_post_category.*
import kotlinx.android.synthetic.main.include_error_layout.*
import kotlin.LazyThreadSafetyMode.NONE

private const val KEY_POST_CATEGORY = "post_category"

private const val CHILD_POST_RECYCLER = 0
private const val CHILD_PROGRESS_BAR = 1
private const val CHILD_ERROR_LAYOUT = 2

class PostCategoryFragment : Fragment(R.layout.fragment_post_category) {
    private val viewModel by viewModels<PostCategoryViewModel>(factoryProducer = App.appComponent::getViewModelFactory)
    private val connectivityMonitor by lazy(NONE, App.appComponent::getConnectivityMonitor)
    private lateinit var pagingAdapter: PagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCategory(getPostCategory())
        initView()
        observeViewModel()
        observeNetworkState()
    }

    private fun getPostCategory(): PostCategory {
        return arguments?.getSerializable(KEY_POST_CATEGORY) as? PostCategory
            ?: error("PostCategoryFragment require a PostCategory")
    }

    private fun initView() {
        initRecyclerView()
        btnRetry.setOnClickListener {
            pagingAdapter.refresh()
        }
    }

    private fun initRecyclerView() {
        pagingAdapter = PagingAdapter().apply {
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

    private fun observeViewModel() {
        viewModel.postPagination
            .subscribe { pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it) }
            .attachToLifecycle(viewLifecycleOwner)
    }

    private fun observeNetworkState() {
        connectivityMonitor.onNetworkAvailable.observe(viewLifecycleOwner, Observer { hasNetwork ->
            if (hasNetwork) {
                pagingAdapter.retry()
            }
        })
    }

    companion object {
        fun newInstance(category: PostCategory) = PostCategoryFragment().apply {
            arguments = bundleOf(KEY_POST_CATEGORY to category)
        }
    }
}

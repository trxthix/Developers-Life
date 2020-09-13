package com.github.trxthix.developerslife.ui.rand

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.trxthix.developerslife.App
import com.github.trxthix.developerslife.R
import com.github.trxthix.developerslife.ui.rand.RandPostViewModel.State.*
import kotlinx.android.synthetic.main.fragment_rand_post.*
import kotlinx.android.synthetic.main.include_error_layout.*
import timber.log.Timber

private const val CHILD_POST_VIEW = 0
private const val CHILD_PROGRESS_BAR = 1
private const val CHILD_ERROR_LAYOUT = 2

class RandPostFragment : Fragment(R.layout.fragment_rand_post) {
    private val viewModel by viewModels<RandPostViewModel> {
        App.appComponent.getViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
        viewModel.getCurrentPost()
    }

    private fun initView() {
        btnPrevious.setOnClickListener {
            viewModel.onPreviousPostClick()
        }

        btnNext.setOnClickListener {
            viewModel.onNextPostClick()
        }

        btnRetry.setOnClickListener {
            viewModel.retry()
        }

        postView.imageLoadListener = { isSuccess ->
            if (isSuccess) {
                viewAnimator.displayedChild = CHILD_POST_VIEW
            } else {
                viewAnimator.displayedChild = CHILD_ERROR_LAYOUT
            }
        }
    }

    private fun observeViewModel() {
        viewModel.hasPreviousPost.observe(viewLifecycleOwner, Observer { hasPrevious ->
            btnPrevious.isEnabled = hasPrevious
        })

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is Loading -> viewAnimator.displayedChild = CHILD_PROGRESS_BAR
                is Success -> postView.bind(state.post)
                is Error -> {
                    Timber.e(state.exception)
                    viewAnimator.displayedChild = CHILD_ERROR_LAYOUT
                }
            }
        })
    }

    companion object {
        fun newInstance() = RandPostFragment()
    }
}
package com.github.trxthix.developerslife.ui.category

import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.include_error_layout.*
import kotlinx.android.synthetic.main.item_load_state.*

private const val CHILD_PROGRESS_BAR = 0
private const val CHILD_ERROR_LAYOUT = 1

class PagingStateViewHolder(
    override val containerView: View,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        btnRetry.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> viewAnimator.displayedChild = CHILD_ERROR_LAYOUT
            is LoadState.Loading -> viewAnimator.displayedChild = CHILD_PROGRESS_BAR
        }
    }
}
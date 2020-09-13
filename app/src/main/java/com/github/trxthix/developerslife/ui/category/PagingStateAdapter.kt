package com.github.trxthix.developerslife.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.github.trxthix.developerslife.R

class PagingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingStateViewHolder>() {
    override fun onBindViewHolder(holder: PagingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PagingStateViewHolder(inflater.inflate(R.layout.item_load_state, parent, false), retry)
    }
}

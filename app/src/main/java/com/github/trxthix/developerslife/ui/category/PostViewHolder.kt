package com.github.trxthix.developerslife.ui.category

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.trxthix.developerslife.data.Post
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_post.*

class PostViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun clear() {
        postView.clearImage()
    }

    fun bind(post: Post) {
        postView.bind(post)
    }
}
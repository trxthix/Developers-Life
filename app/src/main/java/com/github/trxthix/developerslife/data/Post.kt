package com.github.trxthix.developerslife.data

import androidx.annotation.Keep

@Keep
data class PostResult(val result: List<Post>)

@Keep
data class Post(
    val id: Long,
    val date: String,
    val author: String,
    val description: String,
    val gifURL: String?,
    val previewURL: String,
    val votes: Int,
    val commentsCount: Int
) {
    fun getImage(): String = gifURL ?: previewURL
}
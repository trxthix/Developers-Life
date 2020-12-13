package com.github.trxthix.developerslife.data

import com.github.trxthix.developerslife.di.AppSingleton
import javax.inject.Inject

@AppSingleton
class PostMemoryCache @Inject constructor() {
    private val list = mutableListOf<Post>()
    private var index = -1

    fun hasNext(): Boolean {
        return index + 1 < list.size
    }

    fun hasPrevious(): Boolean {
        return index - 1 >= 0
    }

    fun hasCurrent(): Boolean {
        return index > -1
    }

    fun next(): Post {
        if (!hasNext()) throw NoSuchElementException()
        return list[++index]
    }

    fun previous(): Post {
        if (!hasPrevious()) throw NoSuchElementException()
        return list[--index]
    }

    fun current(): Post {
        if (!hasCurrent()) throw NoSuchElementException()
        return list[index]
    }

    fun add(post: Post) {
        index++
        list.add(post)
    }
}
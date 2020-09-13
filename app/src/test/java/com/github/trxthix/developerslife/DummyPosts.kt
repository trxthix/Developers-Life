package com.github.trxthix.developerslife

import com.github.trxthix.developerslife.data.Post


object DummyPosts {

    val posts = listOf(
        Post(0, "10/09/07", "Author 1", "descr 1", "URL", "URL", 100, 100),
        Post(1, "12/09/07", "Author 7", "descr 2", "URL", "URL", 400, 500),
        Post(2, "13/09/07", "Author 6", "descr 3", "URL", "URL", 600, 300),
        Post(3, "14/09/07", "Author 5", "descr 4", "URL", "URL", 700, 400),
        Post(4, "15/09/07", "Author 4", "descr 5", "URL", "URL", 900, 500)
    )

    val ONE get() = posts[1]
    val TWO get() = posts[1]
    val THREE get() = posts[1]
    val FOUR get() = posts[1]
    val FIVE get() = posts[1]
}
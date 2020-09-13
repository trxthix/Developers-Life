package com.github.trxthix.developerslife


import com.github.trxthix.developerslife.data.PostMemoryCache
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PostMemoryCacheTest {

    private lateinit var memoryCache: PostMemoryCache

    @Before
    fun setUp() {
        memoryCache = PostMemoryCache()
    }

    @Test
    fun `initial state is empty`() {
        assertFalse(memoryCache.hasCurrent())
        assertFalse(memoryCache.hasNext())
        assertFalse(memoryCache.hasPrevious())
    }

    @Test
    fun `put and get one post`() {
        memoryCache.add(DummyPosts.ONE)

        assertTrue(memoryCache.hasCurrent())
        assertEquals(memoryCache.current(), DummyPosts.ONE)
    }

    @Test
    fun `put and get some posts`() {
        memoryCache.add(DummyPosts.ONE)
        memoryCache.add(DummyPosts.TWO)

        assertTrue(memoryCache.hasCurrent())
        assertTrue(memoryCache.hasPrevious())
        assertEquals(memoryCache.current(), DummyPosts.TWO)
    }

    @Test
    fun `move to back`(){
        memoryCache.add(DummyPosts.ONE)
        memoryCache.add(DummyPosts.TWO)
        memoryCache.add(DummyPosts.THREE)

        assertEquals(memoryCache.previous(), DummyPosts.TWO)
        assertEquals(memoryCache.previous(), DummyPosts.ONE)

        assertFalse(memoryCache.hasPrevious())
        assertTrue(memoryCache.hasCurrent())
        assertTrue(memoryCache.hasNext())
    }

    @Test
    fun `move to forward`(){
        memoryCache.add(DummyPosts.ONE)
        memoryCache.add(DummyPosts.TWO)
        memoryCache.add(DummyPosts.THREE)

        assertEquals(memoryCache.previous(), DummyPosts.TWO)
        assertEquals(memoryCache.previous(), DummyPosts.ONE)

        assertEquals(memoryCache.next(), DummyPosts.TWO)
        assertEquals(memoryCache.next(), DummyPosts.THREE)

        assertTrue(memoryCache.hasPrevious())
        assertTrue(memoryCache.hasCurrent())
        assertFalse(memoryCache.hasNext())
    }
}
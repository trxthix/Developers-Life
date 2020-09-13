package com.github.trxthix.developerslife

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.trxthix.developerslife.data.Post
import com.github.trxthix.developerslife.data.RandPostRepository
import com.github.trxthix.developerslife.ui.rand.RandPostViewModel
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RandPostViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var randPostRepository: RandPostRepository
    private lateinit var viewModel: RandPostViewModel

    @Before
    fun setUp() {
        randPostRepository = mock(RandPostRepository::class.java)
        viewModel = RandPostViewModel(
            randPostRepository
        )

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `onPreviousPostClick() - success`() {
        val observable = Observable.just(DummyPosts.ONE)
        `when`(randPostRepository.previousPost())
            .thenReturn(observable)

        assertEquals(
            viewModel.state.getOrAwaitValue(),
            RandPostViewModel.State.Success(DummyPosts.ONE)
        )
    }

    @Test
    fun `onPreviousPostClick() - error`() {
        val exception = Exception()
        val observable = Observable.error<Post>(exception)
        `when`(randPostRepository.previousPost())
            .thenReturn(observable)

        viewModel.onPreviousPostClick()

        assertEquals(viewModel.state.getOrAwaitValue(), RandPostViewModel.State.Error(exception))
    }

    @Test
    fun `onNextPostClick() - success`() {
        val observable = Observable.just(DummyPosts.ONE)
        `when`(randPostRepository.nextPost())
            .thenReturn(observable)

        viewModel.onNextPostClick()

        assertEquals(viewModel.state.getOrAwaitValue(), RandPostViewModel.State.Success(DummyPosts.ONE))
    }

    @Test
    fun `onNextPostClick() - error`() {
        val exception = Exception()
        val observable = Observable.error<Post>(exception)
        `when`(randPostRepository.nextPost())
            .thenReturn(observable)

        viewModel.onNextPostClick()

        assertEquals(viewModel.state.getOrAwaitValue(), RandPostViewModel.State.Error(exception))
    }

    @Test
    fun `getCurrentPost() - success`() {
        val observable = Observable.just(DummyPosts.ONE)
        `when`(randPostRepository.currentPost())
            .thenReturn(observable)

        viewModel.getCurrentPost()

        assertEquals(
            viewModel.state.getOrAwaitValue(),
            RandPostViewModel.State.Success(DummyPosts.ONE)
        )
    }

    @Test
    fun `getCurrentPost() - error`() {
        val exception = Exception()
        val observable = Observable.error<Post>(exception)
        `when`(randPostRepository.currentPost())
            .thenReturn(observable)

        viewModel.getCurrentPost()

        assertEquals(viewModel.state.getOrAwaitValue(), RandPostViewModel.State.Error(exception))
    }

    @Test
    fun `retry()`(){
        val observable = Observable.just(DummyPosts.ONE)
        `when`(randPostRepository.currentPost())
            .thenReturn(observable)

        viewModel.retry()

        assertEquals(viewModel.state.getOrAwaitValue(), RandPostViewModel.State.Success(DummyPosts.ONE))
    }
}
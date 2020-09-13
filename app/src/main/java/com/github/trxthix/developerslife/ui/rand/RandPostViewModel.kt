package com.github.trxthix.developerslife.ui.rand

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.trxthix.developerslife.data.Post
import com.github.trxthix.developerslife.data.RandPostRepository
import com.github.trxthix.developerslife.ui.rand.RandPostViewModel.State.*
import com.github.trxthix.developerslife.util.addInto
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RandPostViewModel @Inject constructor(
    private val randPostRepository: RandPostRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val hasPreviousPost: LiveData<Boolean> = randPostRepository.hasPreviousPost

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onPreviousPostClick() {
        loadPost(randPostRepository.previousPost())
    }

    fun onNextPostClick() {
        loadPost(randPostRepository.nextPost())
    }

    fun retry() {
        loadPost(randPostRepository.currentPost())
    }

    fun getCurrentPost() {
        loadPost(randPostRepository.currentPost())
    }

    private fun loadPost(source: Observable<Post>) {
        compositeDisposable.clear()
        source.doOnSubscribe { _state.postValue(Loading) }
            .subscribe(
                { _state.postValue(Success(it)) },
                { _state.postValue(Error(it)) }
            ).addInto(compositeDisposable)
    }

    sealed class State {
        object Loading : State()
        data class Success(val post: Post) : State()
        data class Error(val exception: Throwable) : State()
    }
}
package com.github.trxthix.developerslife.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

private class LifecycleDisposable(disposable: Disposable) : DefaultLifecycleObserver,
    Disposable by disposable {
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (!isDisposed) {
            dispose()
        }
    }
}

fun Disposable.attachToLifecycle(owner: LifecycleOwner) {
    owner.lifecycle.addObserver(LifecycleDisposable(this))
}

fun Disposable.addInto(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
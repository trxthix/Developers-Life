package com.github.trxthix.developerslife.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.trxthix.developerslife.di.AppSingleton
import javax.inject.Inject
import javax.inject.Provider

@AppSingleton
class ViewModelFactory @Inject constructor(
    private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = findViewModel(modelClass)
        return provider.get() as T
    }

    private fun <T : ViewModel> findViewModel(modelClass: Class<T>): Provider<ViewModel> =
        viewModels[modelClass]
            ?: viewModels.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown model class $modelClass. Did you add this VM to the DI module?")
}
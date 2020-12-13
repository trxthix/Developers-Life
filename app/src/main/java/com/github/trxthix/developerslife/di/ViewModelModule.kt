@file:Suppress("unused")

package com.github.trxthix.developerslife.di

import androidx.lifecycle.ViewModel
import com.github.trxthix.developerslife.ui.category.PostCategoryViewModel
import com.github.trxthix.developerslife.ui.rand.RandPostViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {
    @IntoMap
    @Binds
    @ViewModelKey(RandPostViewModel::class)
    fun provideRandPostViewModel(viewModel: RandPostViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(PostCategoryViewModel::class)
    fun providePostCategoryViewModel(viewModel: PostCategoryViewModel): ViewModel
}
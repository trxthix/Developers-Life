package com.github.trxthix.developerslife.di

import android.content.Context
import com.github.trxthix.developerslife.util.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope


@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class AppSingleton

@AppSingleton
@Component(
    modules = [
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }

    fun getViewModelFactory(): ViewModelFactory
}
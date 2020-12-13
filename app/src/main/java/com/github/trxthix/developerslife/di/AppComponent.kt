@file:Suppress("unused")

package com.github.trxthix.developerslife.di

import android.content.Context
import com.github.trxthix.developerslife.util.ConnectivityMonitor
import com.github.trxthix.developerslife.util.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@AppSingleton
@Component(
    modules = [
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {
    fun getConnectivityMonitor(): ConnectivityMonitor
    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}
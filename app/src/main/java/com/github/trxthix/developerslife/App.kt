package com.github.trxthix.developerslife

import android.app.Application
import com.github.trxthix.developerslife.di.AppComponent
import com.github.trxthix.developerslife.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        Timber.plant(Timber.DebugTree())
    }
}
package com.github.trxthix.developerslife.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.ConnectivityManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.trxthix.developerslife.di.AppSingleton
import javax.inject.Inject

@AppSingleton
class ConnectivityMonitor @Inject constructor(context: Context) {
    private val delegate: Delegate

    private val _onNetworkAvailable = MutableLiveData<Boolean>()
    val onNetworkAvailable: LiveData<Boolean> get() = _onNetworkAvailable

    init {
        delegate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Api27(context, _onNetworkAvailable)
        } else {
            PreApi27(context, _onNetworkAvailable)
        }
    }

    private interface Delegate

    @Suppress("DEPRECATION")
    private class PreApi27(
        context: Context,
        private val liveData: MutableLiveData<Boolean>
    ) : Delegate {

        private val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                try {
                    val cm =
                        ContextCompat.getSystemService(context, ConnectivityManager::class.java)!!
                    val info = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(cm, intent)

                    liveData.value = info?.state == NetworkInfo.State.CONNECTED
                } catch (t: Throwable) {
                    liveData.value = true /* true is default value */
                }
            }
        }

        init {
            context.registerReceiver(
                receiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private class Api27(
        context: Context,
        private val liveData: MutableLiveData<Boolean>
    ) : Delegate {

        private val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                liveData.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                liveData.postValue(false)
            }
        }

        init {
            val cm = ContextCompat.getSystemService(context, ConnectivityManager::class.java)!!
            cm.registerDefaultNetworkCallback(networkCallback)
        }
    }
}
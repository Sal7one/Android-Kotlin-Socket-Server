package com.sal7one.serversocket.data

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.RemoteException
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NetworkManager constructor(
    context: Context,
) {
    private var cm: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    fun hasNetworkConnection() = getCurrentNetworkState() == NetworkState.NetworkConnected

    private fun getCurrentNetworkState(): NetworkState {
        return try {
            cm.getNetworkCapabilities(cm.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .let { connected ->
                    if (connected == true) NetworkState.NetworkConnected
                    else NetworkState.NetworkUnavailable
                }
        } catch (e: RemoteException) {
            NetworkState.NetworkUnavailable
        }
    }

    fun observeNetState() = callbackFlow<NetworkState> {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(getCurrentNetworkState()) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(getCurrentNetworkState()) }
            }
        }
        awaitClose {
            cm.unregisterNetworkCallback(callback)
        }
    }.flowOn(Dispatchers.IO)
}

enum class NetworkState {
    NetworkConnected,
    NetworkUnavailable
}
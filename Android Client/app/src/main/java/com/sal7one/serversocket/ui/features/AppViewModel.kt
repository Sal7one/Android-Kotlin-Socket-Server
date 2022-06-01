package com.sal7one.serversocket.ui.features


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sal7one.serversocket.di.NetworkPreferenceDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val dataStoreManager: NetworkPreferenceDataStore
) : ViewModel() {

    private var _connectionStatus = MutableStateFlow(false)
    val connectionStatus get() = _connectionStatus

    val messageToSocket = Channel<String>()

    private var _ipFieldValue = MutableStateFlow("192.168.8.165")
    val ipFieldValue = _ipFieldValue.asStateFlow()

    private var _portFieldValue = MutableStateFlow("4999")
    val portFieldValue = _portFieldValue.asStateFlow()

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreManager.getFromDataStore().collect {
            val ipAddress = it[NetworkPreferenceDataStore.StoredKeys.ipAddress] ?: ""
            val portAddress = it[NetworkPreferenceDataStore.StoredKeys.portAddress] ?: 4999
            _ipFieldValue.value = ipAddress
            _portFieldValue.value = portAddress.toString()
        }
    }

    fun setIpFieldValue(value: String) {
        _ipFieldValue.value = value
    }

    fun setPortFieldValue(value: String) {
        _portFieldValue.value = value
    }

    fun openSocketConnection() =
        viewModelScope.launch(Dispatchers.IO) {
            _connectionStatus.value = !_connectionStatus.value

            val hostAddress = _ipFieldValue.value.replace(hostPattren.toRegex(), "")
            val userPort = _portFieldValue.value.replace(portPattren.toRegex(), "")
            var portAddress = 0
            try {
                portAddress = Integer.parseInt(userPort)
                saveInfoToDataStore(hostAddress, portAddress)
            } catch (exp: NumberFormatException) {
                exp.printStackTrace()

            }

            while (true) {
                try {
                    val socket = Socket(hostAddress, portAddress)
                    val dos = DataOutputStream(socket.getOutputStream())

                    if (_connectionStatus.value) {
                        socket.soTimeout = 4500
                        dos.writeUTF(messageToSocket.receive())
                    }
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: Exception) {
                    _connectionStatus.value = false
                    Log.e("AppViewModel", "$e")
                    break
                }
            }
        }

    private fun saveInfoToDataStore(hostAddress: String, portAddress: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.saveNetworkOptions(
                ipAddress = hostAddress,
                portAddress = portAddress,
            )
        }

    companion object {
        private const val hostPattren = "[^\\d\\.]"
        private const val portPattren = "[^0-9]"
    }
}
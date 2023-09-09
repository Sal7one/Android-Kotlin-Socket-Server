package com.sal7one.serversocket.ui


import android.net.http.NetworkException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sal7one.serversocket.data.AppSensorManager
import com.sal7one.serversocket.data.NetworkManager
import com.sal7one.serversocket.di.NetworkPreferenceDataStore
import com.sal7one.serversocket.di.SensorData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.NoRouteToHostException
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val dataStoreManager: NetworkPreferenceDataStore,
    private val networkManager: NetworkManager,
    private val sensorManager: AppSensorManager,
) : ViewModel() {

    private val _exceptions = MutableStateFlow<Exception?>(null)
    val exceptions = _exceptions.asStateFlow()

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.NOT_CONNECTED)
    val connectionStatus = _connectionStatus.asStateFlow()

    private val _sensors = MutableStateFlow(SensorData())
    val sensors = _sensors.asStateFlow()

    private val messageToSocket = Channel<String>()

    private val _ipFieldValue = MutableStateFlow("192.168.1.1")
    val ipFieldValue: StateFlow<String> = _ipFieldValue.asStateFlow()

    private val _portFieldValue = MutableStateFlow("35642")
    val portFieldValue: StateFlow<String> = _portFieldValue.asStateFlow()

    init {
        fetchInitialData()
        collectSensorData()
    }

    private fun fetchInitialData() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreManager.getFromDataStore().collect {
            val ipAddress = it[NetworkPreferenceDataStore.StoredKeys.ipAddress] ?: ""
            val portAddress = it[NetworkPreferenceDataStore.StoredKeys.portAddress] ?: 35642
            _ipFieldValue.value = ipAddress
            _portFieldValue.value = portAddress.toString()
        }
    }

    fun doesHaveNetwork() : Boolean{
        return networkManager.hasNetworkConnection()
    }
    private fun collectSensorData() {
        viewModelScope.launch {
            sensorManager.sensorChannel
                .receiveAsFlow()
                .collect { sensors ->
                    if (sensors.luminosity != "") {
                        messageToSocket.send(sensors.luminosity)
                        _sensors.update {
                            it.copy(
                                luminosity = sensors.luminosity
                            )
                        }
                    }
                    if (sensors.accelerometer != "") {
                        messageToSocket.send(sensors.accelerometer)
                        _sensors.update {
                            it.copy(
                                accelerometer = sensors.accelerometer
                            )
                        }
                    }
                }
        }
    }

    fun updateIpField(value: String) {
        _ipFieldValue.value = value
    }

    fun updatePortField(value: String) {
        _portFieldValue.value = value
    }

    fun toggleConnection() = viewModelScope.launch(Dispatchers.IO) {
        val cleanedHost = _ipFieldValue.value.replace(HOST_PATTERN.toRegex(), "")
        val cleanedPort = _portFieldValue.value.replace(PORT_PATTERN.toRegex(), "")
        val portAddress = cleanedPort.toIntOrNull() ?: return@launch

        saveNetworkSettings(cleanedHost, portAddress)
        if (connectionStatus.value != ConnectionStatus.CONNECTED) {
            _connectionStatus.value = ConnectionStatus.CONNECTING
            processSocketConnection(cleanedHost, portAddress)
        } else {
            _connectionStatus.value = ConnectionStatus.NOT_CONNECTED
        }
    }

    // TODO server repo
    private suspend fun processSocketConnection(host: String, port: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            while (connectionStatus.value != ConnectionStatus.NOT_CONNECTED) {
                try {
                    val socket = Socket(host, port)
                    _connectionStatus.value = ConnectionStatus.CONNECTED
                    socket.use { socket ->
                        DataOutputStream(socket.getOutputStream()).use { dos ->
                            socket.soTimeout = SOCKET_TIMEOUT
                            dos.writeUTF(messageToSocket.receive())
                            dos.flush()
                        }
                    }
                } catch (exception: NoRouteToHostException) {
                    exception.printStackTrace()
                    _connectionStatus.value = ConnectionStatus.NOT_CONNECTED
                    // TODO separate this layer with error message types
                    _exceptions.emit(exception)
                    break
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    _connectionStatus.value = ConnectionStatus.NOT_CONNECTED
                    _exceptions.emit(exception)
                    break
                }
            }
        }


    private fun saveNetworkSettings(host: String, port: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.saveNetworkOptions(host, port)
        }

    companion object {
        private const val HOST_PATTERN = "[^\\d\\.]"
        private const val PORT_PATTERN = "[^0-9]"
        private const val SOCKET_TIMEOUT = 4500
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.cancel()
    }
}
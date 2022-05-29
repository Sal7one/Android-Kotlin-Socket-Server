package com.sal7one.serversocket


import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sal7one.serversocket.ui.theme.ConnectedSocketButtonColor
import com.sal7one.serversocket.ui.theme.DisconnectedSocketButtonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.Socket

class AppViewModel(primary: Color) : ViewModel() {
    private var _connectionStatus = MutableStateFlow("Connect")
    val connectionStatus = _connectionStatus

    private var _backgroundColor = MutableStateFlow(primary)
    val backgroundColor = _backgroundColor
    val messageToSocket = Channel<String>()

    init {
        when (connectionStatus.value) {
            "Connect" -> _backgroundColor.value = primary
            "Connected" -> _backgroundColor.value = ConnectedSocketButtonColor
            "Disconnected" -> _backgroundColor.value = DisconnectedSocketButtonColor
        }
    }

    fun openSocketConnection() = viewModelScope.launch(Dispatchers.IO) {
        while (true) {
            try {
                val socket = Socket(hostAddress, portAddress)
                socket.soTimeout = 6000
                val dos = DataOutputStream(socket.getOutputStream())
                dos.writeUTF(messageToSocket.receive())
                dos.flush()
                dos.close()
                socket.close()
            } catch (e: Exception) {
                Log.e("APPVIEWMODE", "$e")
            }
        }
    }

    companion object {
        const val hostAddress = "192.168.8.146"
        const val portAddress = 4999
    }
}
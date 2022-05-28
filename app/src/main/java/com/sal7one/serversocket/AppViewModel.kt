package com.sal7one.serversocket


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

class AppViewModel() : ViewModel() {
    private var _connectionStatus = MutableStateFlow("Connect")
    val connectionStatus = _connectionStatus

    private var _backgroundColor = MutableStateFlow(Color.Unspecified)
    val backgroundColor = _backgroundColor

    fun openSocketConnection() = viewModelScope.launch(Dispatchers.IO) {
    }
    companion object{
        val hostAddress = ""
        val portAddress = 0
    }
}
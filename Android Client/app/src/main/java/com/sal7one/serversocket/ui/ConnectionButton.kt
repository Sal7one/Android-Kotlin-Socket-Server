package com.sal7one.serversocket.ui

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sal7one.serversocket.R
import com.sal7one.serversocket.ui.theme.ConnectedSocketButtonColor
import com.sal7one.serversocket.ui.theme.ConnectingSocketButtonColor

@Composable
fun ConnectionButton(connectionStatus: ConnectionStatus, onClickMethod: () -> Unit) {
    val context = LocalContext.current

    val text = when (connectionStatus) {
        ConnectionStatus.CONNECTED -> context.getString(R.string.connected)
        ConnectionStatus.CONNECTING -> context.getString(R.string.connecting)
        else -> {
            context.getString(R.string.connect)
        }
    }

    val backgroundColor = when (connectionStatus) {
        ConnectionStatus.CONNECTED -> ConnectedSocketButtonColor
        ConnectionStatus.CONNECTING -> ConnectingSocketButtonColor
        ConnectionStatus.NOT_CONNECTED -> {
            MaterialTheme.colorScheme.primary
        }
    }
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor),
            onClick = {
                onClickMethod()
            }
        ) {
            Text(text)
        }
}

enum class ConnectionStatus {
    CONNECTED,
    CONNECTING,
    NOT_CONNECTED
}
package com.sal7one.serversocket.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.sal7one.serversocket.R
import com.sal7one.serversocket.ui.theme.ConnectedSocketButtonColor

@Composable
fun ConnectionButton(connectionStatus: Boolean, onClickMethod: () -> Unit) {
    val context = LocalContext.current

    val text = when (connectionStatus) {
        true -> context.getString(R.string.connected)
        else -> {
            context.getString(R.string.connect)
        }
    }
    val backgroundColor = when (connectionStatus) {
        true -> ConnectedSocketButtonColor
        else -> {
            MaterialTheme.colorScheme.primary
        }
    }
    Box(
        contentAlignment = Alignment.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor),
            onClick = { onClickMethod() }) {
            Text(text)
        }
    }
}
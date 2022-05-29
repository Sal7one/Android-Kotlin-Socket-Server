package com.sal7one.serversocket

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun ConnectionButton(btnText: String, btnBackground: Color, onClickMethod: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(btnBackground),
            onClick = { onClickMethod() }) {
            Text(btnText)
        }
    }
}
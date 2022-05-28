package com.sal7one.serversocket

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun ConnectionButton(btnText: String, btnBackground: Color, onClickMethod: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors()
                .also { contentColorFor(backgroundColor = btnBackground) },
            onClick = { onClickMethod() }) {
            Text(btnText)
        }
    }
}
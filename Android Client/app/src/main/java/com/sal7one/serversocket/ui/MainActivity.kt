package com.sal7one.serversocket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sal7one.serversocket.ui.theme.ServerSocketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerSocketTheme {
                val connectionStatus = viewModel.connectionStatus.collectAsState()
                val sensorData by viewModel.sensors.collectAsState()
                val ipField = viewModel.ipFieldValue.collectAsState()
                val portField = viewModel.portFieldValue.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = sensorData.luminosity)
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(text = sensorData.accelerometer)
                        Spacer(modifier = Modifier.height(25.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(value = ipField.value,
                                onValueChange = { viewModel.updateIpField(it) },
                                label = { Text("IP Address") }
                            )
                            OutlinedTextField(value = portField.value,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                onValueChange = { viewModel.updatePortField(it) },
                                label = { Text("PORT") }
                            )
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                        ConnectionButton(connectionStatus.value) {
                            viewModel.toggleConnection()
                        }
                    }
                }
            }
        }
    }
}
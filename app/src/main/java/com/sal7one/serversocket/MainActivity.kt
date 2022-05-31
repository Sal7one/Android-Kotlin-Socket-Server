package com.sal7one.serversocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sal7one.serversocket.ui.theme.ServerSocketTheme
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerSocketTheme {
                val context = LocalContext.current
                val viewModel = AppViewModel()
                val connectionStatus = viewModel.connectionStatus.collectAsState()
                val scope = rememberCoroutineScope()
                var lightData by remember { mutableStateOf(SensorData("")) }
                var accelData by remember { mutableStateOf(SensorData("")) }
                val ipField = remember { mutableStateOf(TextFieldValue("192.168.8.165")) }
                val portField = remember { mutableStateOf(TextFieldValue("4999")) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = lightData.luminosity) {
                        viewModel.messageToSocket.send(lightData.luminosity)
                    }
                    LaunchedEffect(key1 = accelData.accelerometer) {
                        viewModel.messageToSocket.send(accelData.accelerometer)
                    }
                    DisposableEffect(Unit) {
                        val dataManager = SensorHandler(context)
                        val lightJob = scope.launch {
                            dataManager.lightData
                                .receiveAsFlow()
                                .collect {
                                    lightData = it
                                }
                        }

                        val acellJob = scope.launch {
                            dataManager.acellData
                                .receiveAsFlow()
                                .collect {
                                    accelData = it
                                }
                        }
                        onDispose {
                            dataManager.cancel()
                            lightJob.cancel()
                            acellJob.cancel()
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = lightData.luminosity)
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(text = accelData.accelerometer)
                        Spacer(modifier = Modifier.height(25.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(value = ipField.value,
                                onValueChange = { ipField.value = it },
                                label = { Text("IP Address") }
                            )
                            OutlinedTextField(value = portField.value,
                                keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Number),
                                onValueChange = { portField.value = it },
                                label = { Text("PORT") }
                            )
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                        ConnectionButton(connectionStatus.value) {
                            viewModel.openSocketConnection(ipField.value, portField.value)
                        }
                    }
                }
            }
        }
    }
}
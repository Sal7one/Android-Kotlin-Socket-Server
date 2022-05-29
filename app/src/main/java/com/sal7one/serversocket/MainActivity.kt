package com.sal7one.serversocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sal7one.serversocket.ui.theme.ServerSocketTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerSocketTheme {
                val context = LocalContext.current
                val viewModel = AppViewModel(MaterialTheme.colorScheme.primary)
                val connectionStatus = viewModel.connectionStatus.collectAsState()
                val btnColor = viewModel.backgroundColor.collectAsState()
                val scope = rememberCoroutineScope()
                var lightSensorData by remember { mutableStateOf(LightSensorData("")) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = lightSensorData){

                        viewModel.messageToSocket.send(lightSensorData.luminosity)
                    }
                    DisposableEffect(Unit) {
                        val dataManager = SensorHandler(context)
                        val job = scope.launch {
                            dataManager.sensorData
                                .receiveAsFlow()
                                .collect {
                                    lightSensorData = it
                                }
                        }

                        onDispose {
                            dataManager.cancel()
                            job.cancel()
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Luminosity: ${lightSensorData.luminosity}")
                        Spacer(modifier = Modifier.height(25.dp))

                        ConnectionButton(connectionStatus.value, btnColor.value) {
                            viewModel.openSocketConnection()
                        }
                    }
                }
            }
        }
    }
}
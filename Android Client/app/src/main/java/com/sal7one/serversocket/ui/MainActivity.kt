package com.sal7one.serversocket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sal7one.serversocket.R
import com.sal7one.serversocket.ui.theme.ServerSocketTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.NoRouteToHostException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerSocketTheme {
                val viewModel: AppViewModel = viewModel()
                val connectionStatus by viewModel.connectionStatus.collectAsState()
                val sensorData by viewModel.sensors.collectAsState()
                val ipField = viewModel.ipFieldValue.collectAsState()
                val portField = viewModel.portFieldValue.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val context = LocalContext.current

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { padding ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            ActiveTextScope {
                                Text(text = sensorData.luminosity)
                                Spacer(modifier = Modifier.height(25.dp))
                                Text(text = sensorData.accelerometer)
                                Spacer(modifier = Modifier.height(25.dp))
                            }

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
                            ConnectionButton(connectionStatus) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.toggleConnection()
                                }
                            }
                        }
                    }
                    LaunchedEffect(key1 = snackbarHostState) {
                        viewModel.exceptions.collectLatest { exception ->
                            val errorMessage = when {
                                exception is NoRouteToHostException -> context.getString(R.string.no_host)
                                viewModel.doesHaveNetwork() && exception is ConnectException -> context.getString(
                                    R.string.no_host
                                )

                                !viewModel.doesHaveNetwork() && exception is ConnectException -> context.getString(
                                    R.string.check_the_connection
                                )

                                else -> context.getString(R.string.something_wrong)
                            }
                            if (exception != null)
                                snackbarHostState.showSnackbar(message = errorMessage)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ActiveTextScope(content: @Composable () -> Unit) {
    content()
}
package com.sal7one.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.BindException
import java.net.ServerSocket

class SocketServerRepository {
    private var serverSocket: ServerSocket? = null

    // Starts the server on the given port
    fun startServer(port: Int) {
        try {
            serverSocket = ServerSocket(port)
            while (true) {
                val client = serverSocket?.accept()
                val reader = BufferedReader(InputStreamReader(client?.inputStream))
                reader.lineSequence().forEach { sensorData ->
                    val sanitizedData = sensorData.replace("\\s".toRegex(), "")
                    if (sanitizedData.isNotEmpty()) {
                        processSensorData(sanitizedData)
                    }
                }
            }
        } catch (e: BindException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            serverSocket?.close()
        }
    }

    // Processes sensor data and updates labels
    private fun processSensorData(sensorData: String) {
        val sensorName = getSensorName(sensorData)
        val cleanedData = "${filterSensorName(sensorName)}: ${filterResult(sensorData)}"

        when {
            sensorName.contains("Luminosity") -> AppFrame.luminosityLabel.text = cleanedData
            sensorName.contains("Unknown Sensor:") -> AppFrame.luminosityLabel.text = cleanedData
            else -> AppFrame.otherSensorLabel.text = cleanedData
        }
    }

    // Filters out non-numeric characters from the sensor result
    private fun filterResult(sensorData: String): String =
        sensorData.replace("[^\\d.]".toRegex(), "")

    // Filters out non-word characters from the sensor name
    private fun filterSensorName(sensorName: String): String =
        sensorName.replace("[^\\w]".toRegex(), "")

    // Extracts the sensor name from the sensor data string
    private fun getSensorName(sensorData: String): String {
        return try {
            sensorData.replace("(\\u0020)|(\\u0015)".toRegex(), "")
                .substring(0, sensorData.indexOf(":"))
        } catch (e: Exception) {
            "Unknown Sensor:"
        }
    }
}

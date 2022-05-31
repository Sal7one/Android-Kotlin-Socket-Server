package com.sal7one.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket


class SocketServer {
    private var serverSocket: ServerSocket? = null

    fun start(port: Int) {
        try {
            serverSocket = ServerSocket(port)
            while (true) {
                val client = serverSocket!!.accept()
                val readerType = InputStreamReader(client.inputStream)
                val reader = BufferedReader(readerType)

                reader.lineSequence().forEach {
                    if (it != " " && it != "") {
                        val sensorName = getSensorName(it)
                        val cleanedData = "$sensorName: ${Controller.filterResult(it)}"
                        if (sensorName.contains("Luminosity")) {
                            AppFrame.luminosityLabel.text = cleanedData
                        } else {
                            AppFrame.OtherSensorLabel.text = cleanedData
                        }
                    }
                }
            }

        } catch (excep: Exception) {
            excep.printStackTrace()
        }
    }

    private fun getSensorName(sensorData: String): String {
        return try {
            sensorData.replace("(\\u0020)|(\\u0015)".toRegex(), "").substring(0, sensorData.indexOf(":"))
        } catch (exception: Exception) {
            "Luminosity"
        }
    }
}
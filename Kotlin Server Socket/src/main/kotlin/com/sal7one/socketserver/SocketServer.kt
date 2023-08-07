package com.sal7one.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.BindException
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
                reader.lineSequence().forEach { sensorData ->
                    if (sensorData != "" && sensorData.isNotEmpty()) {
                        val sensorName = Controller.getSensorName(sensorData)
                        val cleanedData = "${Controller.filterSensorName(sensorName)}: ${Controller.filterResult(sensorData)}"
                        if (sensorName.contains("Luminosity")) {
                            AppFrame.luminosityLabel.text = cleanedData
                        } else if(sensorName.contains("Unknown Sensor:")){
                            AppFrame.luminosityLabel.text = cleanedData
                        }else{
                            AppFrame.OtherSensorLabel.text = cleanedData
                        }
                    }
                }
            }
        } catch (netBindExcep: BindException){
            netBindExcep.printStackTrace()
        }catch (excep: Exception) {
            serverSocket?.close()
            excep.printStackTrace()
        }
    }
}
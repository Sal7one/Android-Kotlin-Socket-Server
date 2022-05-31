package com.sal7one.socketserver

class Controller {
    private val server = SocketServer()

    init {
        AppFrame()
        server.start(SERVER_PORT)
    }

    companion object {
        fun filterResult(sensorData: String): String {
            return sensorData.replace(("[^\\d.]").toRegex(), "")
        }

        fun filterSensorName(sensorName: String): String {
        return sensorName.replace(("[^\\w]").toRegex(), "")
        }
    }
}
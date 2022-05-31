package com.sal7one.socketserver

class Controller {
    private val server = SocketServer()

    init {
        AppFrame()
        server.start(SERVER_PORT)
    }

    companion object {
        fun filterResult(sensorData: String): String {
            return sensorData.replace(("[^\\d.]").toRegex(), "").replace("(\\u0020)|(\\u0015)".toRegex(), "")
        }
    }
}
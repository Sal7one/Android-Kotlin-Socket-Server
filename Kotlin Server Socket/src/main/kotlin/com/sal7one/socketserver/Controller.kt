package com.sal7one.socketserver

import java.net.InetAddress


class Controller(private val serverRepository: SocketServerRepository){
    fun startServer() {
        serverRepository.startServer(SERVER_PORT)
    }

    fun getServerIP(): String? {
        return try {
            InetAddress.getLocalHost().hostAddress.toString()
        } catch (exception: Exception) {
            exception.printStackTrace().toString()
            null
        }
    }
}
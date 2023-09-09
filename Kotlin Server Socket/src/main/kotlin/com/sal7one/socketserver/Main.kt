package com.sal7one.socketserver

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

fun main() {
    val serverRepository = SocketServerRepository()
    val controller = Controller(serverRepository)
    initUi(controller.getServerIP())
    controller.startServer()
}

private fun initUi(serverIP: String?) {
    val appFrame = AppFrame(serverIP)
    appFrame.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(windowEvent: WindowEvent) {
            exitDialog(appFrame)
        }
    })
}
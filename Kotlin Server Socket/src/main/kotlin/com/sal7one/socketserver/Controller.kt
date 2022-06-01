package com.sal7one.socketserver

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JOptionPane
import kotlin.system.exitProcess


class Controller {
    private val server = SocketServer()

    init {
        val appFrame = AppFrame()
        appFrame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(windowEvent: WindowEvent) {
                if (JOptionPane.showConfirmDialog(
                        appFrame,
                        "Shutdown server?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    ) == JOptionPane.YES_OPTION
                ) {
                    exitProcess(0)
                }
            }
        })
        server.start(SERVER_PORT)
    }

    companion object {
        fun filterResult(sensorData: String): String {
            return sensorData.replace(("[^\\d.]").toRegex(), "")
        }

        fun filterSensorName(sensorName: String): String {
            return sensorName.replace(("[^\\w]").toRegex(), "")
        }

        fun getSensorName(sensorData: String): String {
            return try {
                sensorData.replace("(\\u0020)|(\\u0015)".toRegex(), "").substring(0, sensorData.indexOf(":"))
            } catch (exception: Exception) {
                "Unknown Sensor:"
            }
        }
    }
}
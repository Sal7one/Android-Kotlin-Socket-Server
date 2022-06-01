package com.sal7one.socketserver

import java.awt.Color
import java.awt.Font
import java.net.DatagramSocket
import java.net.InetAddress
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel


class AppFrame : JFrame() {

    companion object {
        var luminosityLabel = JLabel("Not Connected...")
        var OtherSensorLabel = JLabel("Not Connected...")
    }

    private val labelFont = Font("SansSerif", Font.BOLD, 24)

    init {
        title = "Sensor Socket"
        isVisible = true
        layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        setSize(400, 250)
        setLocation(500, 500)
        contentPane.background = Color.DARK_GRAY
        luminosityLabel.font = labelFont
        luminosityLabel.foreground = Color.CYAN
        OtherSensorLabel.font = labelFont
        OtherSensorLabel.foreground = Color.CYAN
        add(luminosityLabel)
        add(OtherSensorLabel)
        networkInfo()
    }

    private fun networkInfo() {
        try {
            val socket = DatagramSocket()
            socket.connect(InetAddress.getByName("8.8.8.8"), 80)
            val ip = socket.localAddress.hostAddress
            val ipLabel = JLabel("IP: $ip")
            val portLabel = JLabel("PORT: $SERVER_PORT")
            ipLabel.foreground = Color.WHITE
            portLabel.foreground = Color.WHITE
            this.add(ipLabel)
            this.add(portLabel)
        } catch (excep: Exception) {
            luminosityLabel.text = excep.toString()
            excep.printStackTrace()
        }
    }
}
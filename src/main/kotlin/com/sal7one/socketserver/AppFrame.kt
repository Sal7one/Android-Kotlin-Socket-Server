package com.sal7one.socketserver

import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
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
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
        layout = FlowLayout()
        setSize(370, 210)
        setLocation(500, 500)
        contentPane.background = Color.DARK_GRAY
        luminosityLabel.font = labelFont
        luminosityLabel.foreground = Color.CYAN
        OtherSensorLabel.font = labelFont
        OtherSensorLabel.foreground = Color.CYAN
        add(luminosityLabel)
        add(OtherSensorLabel)
    }
}
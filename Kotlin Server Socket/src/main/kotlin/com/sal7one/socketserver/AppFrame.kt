package com.sal7one.socketserver

import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.Toolkit
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.WindowConstants

class AppFrame(ip: String?) : JFrame() {
    companion object {
        val luminosityLabel = JLabel("Luminosity: Not Connected...")
        val otherSensorLabel = JLabel("Accelerometer: Not Connected...")
    }
    private val panel = JPanel()

    init {
        // Frame settings
        title = "Sensor Socket"
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)  // Set the layout on panel
        panel.background = Color.DARK_GRAY
        panel.border = BorderFactory.createEmptyBorder(10, 20, 20, 20)
        contentPane = panel  // Set the panel as content pane
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE;
        // Create UI elements
        if (ip != null) buildUiElements(ip) else showError()

        // Size and location settings
        pack()  // Size the frame to fit the content
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val width: Int = (screenSize.width - size.width) / 2
        val height: Int = (screenSize.height - size.height) / 2
        setLocation(width, height)

        // Make frame visible after setting all its properties
        isVisible = true
    }

    private fun showError() {
        val label = JLabel("Error Getting IP", SwingConstants.CENTER)
        applyStyles(label,  Font("SansSerif", Font.BOLD, 30), Color.RED, Component.CENTER_ALIGNMENT)
        panel.add(label)
    }

    private fun buildUiElements(ip: String) {
        val labelFont = Font("SansSerif", Font.BOLD, 22)
        val centerAlignment = Component.CENTER_ALIGNMENT

        // Create and customize new labels
        val ipLabel = JLabel("IP: $ip")
        val portLabel = JLabel("PORT: $SERVER_PORT")

        applyStyles(luminosityLabel, labelFont, Color.CYAN, centerAlignment)
        applyStyles(otherSensorLabel, labelFont, Color.CYAN, centerAlignment)

        val networkInfo = Font("SansSerif", Font.BOLD, 18)
        applyStyles(ipLabel, networkInfo, Color.WHITE, centerAlignment)
        applyStyles(portLabel, networkInfo, Color.WHITE, centerAlignment)

        panel.add(luminosityLabel)
        panel.add(otherSensorLabel)
        panel.add(ipLabel)
        panel.add(portLabel)
    }

    private fun applyStyles(label: JLabel, font: Font, color: Color, alignment: Float) {
        label.font = font
        label.foreground = color
        label.alignmentX = alignment
    }
}
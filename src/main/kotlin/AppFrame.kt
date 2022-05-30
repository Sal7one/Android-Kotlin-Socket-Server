import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import java.net.ServerSocket
import java.util.*
import javax.swing.JFrame
import javax.swing.JLabel

class AppFrame() : JFrame() {
    private var luminosity = JLabel("Not Connected...")
    private val labelFont = Font("SansSerif", Font.BOLD, 24)

    init {
        title = "Sensor Socket"
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
        setSize(400, 85)
        setLocation(500, 500)
        layout = FlowLayout()
        contentPane.background = Color.WHITE
        luminosity.font = labelFont
        add(luminosity)
        startWebSocket()
    }

    private fun changeColors(phoneLuminosity: String, backgroundColor: Color, labelColor: Color) {
        contentPane.background = backgroundColor
        luminosity.text = "Light Luminity is: $phoneLuminosity"
        luminosity.foreground = labelColor
    }

    @Throws(Exception::class)
    fun startWebSocket() {
        val serverSocket = ServerSocket(4999)
        while (true) {
            val client = serverSocket.accept()
            val reader = Scanner(client.getInputStream())
            while (reader.hasNextLine()) {
                val sensorData = reader.nextLine()
                val cleanSensorData = sensorData.toString().replace(("[^\\d.]").toRegex(), "")

                if (cleanSensorData != "" && cleanSensorData.isNotEmpty() && cleanSensorData != " ") {
                    try {
                        if (cleanSensorData.toDouble() < 100)
                            changeColors(cleanSensorData, Color.BLACK, Color.WHITE)
                        else if (cleanSensorData.toDouble() < 500)
                            changeColors(cleanSensorData, Color.GRAY, Color.CYAN)
                        else
                            changeColors(cleanSensorData, Color.WHITE, Color.BLACK)
                    } catch (numExcp: NumberFormatException) {
                        numExcp.printStackTrace()
                    }
                }
            }
        }
    }
}
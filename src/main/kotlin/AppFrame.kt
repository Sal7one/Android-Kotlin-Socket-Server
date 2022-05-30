import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JFrame
import javax.swing.JLabel

class AppFrame(uiData: Channel<String>) : JFrame() {
    private var luminosityLabel = JLabel("Not Connected...")
    private val labelFont = Font("SansSerif", Font.BOLD, 24)


    init {
        title = "Sensor Socket"
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
        setSize(400, 85)
        setLocation(500, 500)
        layout = FlowLayout()
        contentPane.background = Color.WHITE
        luminosityLabel.font = labelFont
        add(luminosityLabel)

        GlobalScope.launch {
            uiData.receiveAsFlow().collect{updateData(it)}
        }
    }

    private fun changeColors(phoneLuminosity: String, backgroundColor: Color, labelColor: Color) {
        contentPane.background = backgroundColor
        luminosityLabel.text = "Light Luminosity is: $phoneLuminosity"
        luminosityLabel.foreground = labelColor
    }

    private fun updateData(uiData: String) {
        luminosityLabel.text = uiData
    }

    private fun filterResult(sensorData: String): String {
        return sensorData.replace(("[^\\d.]").toRegex(), "")
    }
}
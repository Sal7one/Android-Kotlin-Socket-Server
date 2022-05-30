import kotlinx.coroutines.channels.Channel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class SensorServer(serverId: Channel<Int>, serverData: Channel<String>) {
//    private var serverSocket: ServerSocket? = null
//    private var clientSocket: Socket? = null
//    private var input: BufferedReader? = null

    val serverId: Channel<Int> = Channel(Channel.UNLIMITED)
    val serverData: Channel<String> = Channel(Channel.UNLIMITED)
//
//    fun start(port: Int) {
//        serverSocket = ServerSocket(SERVER_PORT)
//        clientSocket = serverSocket!!.accept()
//        input = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
//    }

    private fun filterResult(sensorData: String): String {
        return sensorData.replace(("[^\\d.]").toRegex(), "")
    }

//    @Throws(Exception::class)
//    fun startWebSocket() {
//        val serverSocket = ServerSocket(4999)
//        while (true) {
//            val client = serverSocket.accept()
//            val reader = Scanner(client.getInputStream())
//            while (reader.hasNextLine()) {
//                val sensorData = reader.nextLine()
//                val cleanSensorData = sensorData.toString().replace(("[^\\d.]").toRegex(), "")
//                if (cleanSensorData != "" && cleanSensorData.isNotEmpty() && cleanSensorData != " ") {
//                    try {
//                        notifyListeners()
//                    } catch (numException: NumberFormatException) {
//                        numException.printStackTrace()
//                    }
//                }
//            }
//        }
//    }
}
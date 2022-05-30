import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket


class EchoMultiServer {
    private var serverSocket: ServerSocket? = null
    val serverData: Channel<String> = Channel(Channel.UNLIMITED)

    suspend fun start(port: Int) {
        while (true){
            serverSocket = ServerSocket(port)
            val serverInstance = EchoClientHandler(serverSocket!!.accept())
            serverInstance.run()
            serverInstance.serverResult.receiveAsFlow().collect{
                serverData.trySend(it)
            }
        }
    }
}

private class EchoClientHandler(private val clientSocket: Socket) {
    private var out: PrintWriter? = null
    private var inputData: BufferedReader? = null
    val serverResult: Channel<String> = Channel(Channel.UNLIMITED)

    suspend fun run() = coroutineScope {
        out = PrintWriter(clientSocket.getOutputStream(), true)
        inputData = BufferedReader(
            InputStreamReader(clientSocket.getInputStream())
        )
        var inputLine: String

        while (inputData?.readLine() !=null) {
                inputLine = inputData.toString()

            if ("DESTROY_SERVER" == inputLine) {
                serverResult.trySend("Client Requested Server to be destroyed..")
                break
            }
            serverResult.trySend(inputLine)
        }
        inputData!!.close()
        out!!.close()
        clientSocket.close()
    }
}
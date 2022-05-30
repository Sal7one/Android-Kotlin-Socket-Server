import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class Controller {
    private val server = EchoMultiServer()
    val uiData: Channel<String> = Channel(Channel.UNLIMITED)

    init {

        GlobalScope.launch {
            server.start(SERVER_PORT)
            observeData()
        }
        AppFrame(uiData)
    }

    private suspend fun observeData() = coroutineScope {
        server.serverData
            .receiveAsFlow()
            .collect {
                uiData.trySend(it)
            }
    }
}
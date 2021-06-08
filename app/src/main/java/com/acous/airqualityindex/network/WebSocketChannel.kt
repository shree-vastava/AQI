package com.acous.airqualityindex.network

import com.acous.airqualityindex.data.CityList
import com.acous.airqualityindex.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketChannel(private val scope: CoroutineScope) : IWebSocketChannel {

    private val socket: WebSocket
    private val incoming = Channel<Resource<CityList>>()
    private val incomingFlow: Flow<Resource<CityList>> = incoming.consumeAsFlow()

    init {
        val okHttpClient = OkHttpClient.Builder()
                .pingInterval(5, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder()
                .url("ws://city-ws.herokuapp.com/")
                .build()

        socket = okHttpClient.newWebSocket(request, WebSocketChannelListener(incoming))

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        okHttpClient.dispatcher.executorService.shutdown()
    }

    override fun getIncoming(): Flow<Resource<CityList>> {
        return incomingFlow
    }

    override fun isClosed(): Boolean {
        return incoming.isClosedForReceive
    }

    override fun close(
            code: Int,
            reason: String?
    ) {
        scope.launch(Dispatchers.IO) {
            socket.close(code, reason) // note: Channels will close in WebSocket.onClosed
        }
    }

    override fun send(data: CityList) {

    }

    inner class WebSocketChannelListener(private val incoming: Channel<Resource<CityList>>) : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {}

        override fun onMessage(webSocket: WebSocket, text: String) {
            println("OnMessage: $text")
            scope.launch(Dispatchers.IO) {
                incoming.send(Resource.success(CityList(text)))
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {}

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            incoming.close()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            scope.launch {
                incoming.send(Resource.error("Some error occurred", null))
            }
            incoming.close(t)
        }
    }
}
package jkly.slack.rtm

import com.google.gson.Gson
import jkly.extension.toObject
import jkly.slack.rtm.event.RtmEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ws.WebSocket
import okhttp3.ws.WebSocketCall
import okhttp3.ws.WebSocketListener
import okio.Buffer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class RtmSession(val client: OkHttpClient, val url: String) {
    val gson = Gson()

    fun connect(vararg handlers: EventHandler) {
        val request = Request.Builder().get().url(url).build()
        val connectCall = WebSocketCall.create(client, request)

        connectCall.enqueue(object: WebSocketListener {
            lateinit var sender: MessageSender
            override fun onOpen(webSocket: WebSocket?, response: Response?) {
                this.sender = MessageSender(webSocket!!)
            }

            override fun onPong(payload: Buffer?) {
            }

            override fun onClose(code: Int, reason: String?) {
                if (code == 1000) {
                    LOGGER.info("Closing web socket normally $url: $reason")
                } else {
                    throw RuntimeException("Web socket closed $url: $reason")
                }
            }

            override fun onFailure(e: IOException?, response: Response?) {
            }

            override fun onMessage(message: ResponseBody) {
                val eventJson = message.string()
                val rtmEvent = eventJson.toObject<RtmEvent>(gson)

                LOGGER.debug("Event JSON: $eventJson")

                if (rtmEvent.type == "hello") {
                    LOGGER.info("Received hello from RTM WebSocket: $url")
                } else {
                    handlers.filter { it.accept(rtmEvent.type) }
                            .forEach { it.handle(eventJson, sender) }
                }
            }

        })
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(RtmSession::class.java)
    }
}
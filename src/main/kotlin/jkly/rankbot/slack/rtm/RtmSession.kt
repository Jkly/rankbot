package jkly.rankbot.slack.rtm

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jkly.rankbot.slack.rtm.event.RtmEvent
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
    val gson: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    fun connect(vararg handlers: EventHandler) {
        val request = Request.Builder().get().url(url).build()
        val connectCall = WebSocketCall.create(client, request)

        connectCall.enqueue(object: WebSocketListener {
            lateinit var webSocket: WebSocket
            override fun onOpen(webSocket: WebSocket?, response: Response?) {
                this.webSocket = webSocket!!
            }

            override fun onPong(payload: Buffer?) {
            }

            override fun onClose(code: Int, reason: String?) {
            }

            override fun onFailure(e: IOException?, response: Response?) {
            }

            override fun onMessage(message: ResponseBody) {
                val eventJson = message.string()
                val rtmEvent = gson.fromJson(eventJson, RtmEvent::class.java)

                LOGGER.debug("Event JSON: $eventJson")

                if (rtmEvent.type == "hello") {
                    LOGGER.info("Received hello from RTM WebSocket: $url")
                } else {
                    handlers.filter { it.accept(rtmEvent.type) }
                            .forEach { it.handle(eventJson, webSocket) }
                }
            }

        })
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(RtmSession::class.java)
    }
}
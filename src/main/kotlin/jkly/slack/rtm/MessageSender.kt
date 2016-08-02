package jkly.slack.rtm

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ws.WebSocket
import java.util.concurrent.atomic.AtomicLong

class MessageSender(val socket: WebSocket) {

    private var messageId = AtomicLong(0L)
    val gson = Gson()

    fun send(channel:String, message:String) {
        val request = RequestBody.create(WebSocket.TEXT, gson.toJson(Message(messageId.incrementAndGet(), channel, message)))
        socket.sendMessage(request)
    }

    companion object {
        val MEDIA_TYPE = MediaType.parse("application/json")
    }
}

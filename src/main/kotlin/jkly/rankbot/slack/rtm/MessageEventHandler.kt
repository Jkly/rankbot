package jkly.rankbot.slack.rtm

import com.google.gson.Gson
import jkly.rankbot.slack.rtm.event.MessageEvent
import okhttp3.ws.WebSocket

abstract class MessageEventHandler : EventHandler {
    val gson = Gson()

    abstract fun handle(event: MessageEvent, socket: WebSocket)

    override fun handle(json: String, socket: WebSocket) {
        handle(gson.fromJson(json, MessageEvent::class.java), socket)
    }

    override fun accept(type: String): Boolean {
        return type == TYPE
    }

    companion object {
        val TYPE = "message"
    }
}

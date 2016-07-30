package jkly.rankbot.slack.rtm

import com.google.gson.Gson
import jkly.rankbot.slack.rtm.event.MessageEvent

abstract class MessageEventHandler : EventHandler {
    val gson = Gson()

    abstract fun handle(event: MessageEvent, sender: MessageSender)

    override fun handle(json: String, sender: MessageSender) {
        handle(gson.fromJson(json, MessageEvent::class.java), sender)
    }

    override fun accept(type: String): Boolean {
        return type == TYPE
    }

    companion object {
        val TYPE = "message"
    }
}

package jkly.slack.rtm

import com.google.gson.Gson
import jkly.extension.toObject
import jkly.slack.rtm.event.MessageEvent

abstract class MessageEventHandler : EventHandler {
    val gson = Gson()

    abstract fun handle(event: MessageEvent, sender: MessageSender)

    override fun handle(json: String, sender: MessageSender) {
        handle(json.toObject<MessageEvent>(gson), sender)
    }

    override fun accept(type: String): Boolean {
        return type == TYPE
    }

    companion object {
        val TYPE = "message"
    }
}

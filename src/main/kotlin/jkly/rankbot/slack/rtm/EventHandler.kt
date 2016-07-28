package jkly.rankbot.slack.rtm

import okhttp3.ws.WebSocket

interface EventHandler {

    fun accept(type:String):Boolean

    fun handle(json: String, socket: WebSocket)
}
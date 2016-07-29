package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.rankbot.slack.rtm.MessageEventHandler
import jkly.rankbot.slack.rtm.event.MessageEvent
import okhttp3.ws.WebSocket

class GetRankings(val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, socket: WebSocket) {
        if (event.text == "rankings") {
        }
    }
}
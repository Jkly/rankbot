package jkly.rankbot

import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.slack.rtm.MessageEventHandler
import jkly.rankbot.slack.rtm.event.MessageEvent
import okhttp3.ws.WebSocket

class MatchMessageHandler(val eloCalculator: EloCalculator, val rankStore: RankStore) : MessageEventHandler() {
    override fun handle(event: MessageEvent, socket: WebSocket) {

    }

}
package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.slack.rtm.MessageEventHandler
import jkly.slack.rtm.MessageSender
import jkly.slack.rtm.event.MessageEvent

class GetRankings(val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, sender: MessageSender) {
        if (event.text == "rankings") {

        }
    }
}
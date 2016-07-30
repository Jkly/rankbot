package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.rankbot.slack.rtm.MessageEventHandler
import jkly.rankbot.slack.rtm.MessageSender
import jkly.rankbot.slack.rtm.event.MessageEvent

class GetRankings(val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, sender: MessageSender) {
        if (event.text == "rankings") {

        }
    }
}
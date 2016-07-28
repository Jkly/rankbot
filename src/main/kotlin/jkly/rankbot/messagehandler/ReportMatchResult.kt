package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.slack.rtm.MessageEventHandler
import jkly.rankbot.slack.rtm.event.MessageEvent
import okhttp3.ws.WebSocket
import java.util.regex.Pattern

class ReportMatchResult(val eloCalculator: EloCalculator, val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, socket: WebSocket) {
        val matcher = PATTERN.matcher(event.text)
        if (matcher.matches()) {
            val winner = matcher.group("winner")
            val loser = matcher.group("loser")
        }
    }


    companion object {
        val USERNAME_PATTERN = "[a-z0-9][a-z0-9._-]*"
        val PATTERN: Pattern = Pattern.compile("@(?<winner>${USERNAME_PATTERN})\\w+beat\\w+@(?<loser>${USERNAME_PATTERN})")
    }
}
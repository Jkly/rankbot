package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.elo.Match
import jkly.rankbot.elo.Player
import jkly.rankbot.slack.SlackClient
import jkly.rankbot.slack.rtm.MessageEventHandler
import jkly.rankbot.slack.rtm.event.MessageEvent
import okhttp3.ws.WebSocket
import java.util.regex.Pattern

class ReportMatchResult(val client: SlackClient, val eloCalculator: EloCalculator,
                        val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, socket: WebSocket) {
        val matcher = PATTERN.matcher(event.text)
        if (matcher.matches()) {
            val winner = getPlayer(matcher.group("winner"))
            val loser = getPlayer(matcher.group("loser"))
            val updatedRatings = eloCalculator.updateRatings(Match(winner, loser))

            playerRepository.save(updatedRatings.winner)
            playerRepository.save(updatedRatings.loser)
        }
    }

    private fun getPlayer(username: String): Player {
        val id = client.userList().members
                .filter { it.name == username }
                .map { it.id }
                .first()
        return playerRepository.get(id)
    }

    companion object {
        val PATTERN: Pattern = Pattern.compile("@(?<winner>$USERNAME)\\w+beat\\w+@(?<loser>$USERNAME)")
    }
}
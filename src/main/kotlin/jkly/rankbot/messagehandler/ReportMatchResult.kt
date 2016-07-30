package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.rankbot.SlackPlayer
import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.elo.Match
import jkly.rankbot.elo.Player
import jkly.rankbot.slack.SlackClient
import jkly.rankbot.slack.UserListResponse
import jkly.rankbot.slack.rtm.MessageEventHandler
import jkly.rankbot.slack.rtm.MessageSender
import jkly.rankbot.slack.rtm.event.MessageEvent
import java.util.regex.Pattern

class ReportMatchResult(val client: SlackClient, val eloCalculator: EloCalculator,
                        val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, sender: MessageSender) {
        val matcher = PATTERN.matcher(event.text)
        if (matcher.matches()) {
            val userList = client.userList()

            val winner = userList.getPlayer(matcher.group("winner"))
            val loser = userList.getPlayer(matcher.group("loser"))

            val (updatedWinner, updatedLoser) = eloCalculator.updateRatings(playMatch(winner.player, loser.player))

            playerRepository.save(winner.copy(player = updatedWinner))
            playerRepository.save(loser.copy(player = updatedLoser))
        }
    }

    private fun playMatch(winner: Player, loser: Player): Match {
        return Match(winner.updateGamesPlayed(), loser.updateGamesPlayed())
    }

    private fun Player.updateGamesPlayed(): Player {
        return this.copy(gamesPlayed = this.gamesPlayed+1)
    }

    private fun UserListResponse.getPlayer(username: String): SlackPlayer {
        val id = this.members
                .filter { it.name == username }
                .map { it.id }
                .first()
        return playerRepository.get(id)
    }

    companion object {
        val PATTERN: Pattern = Pattern.compile("@(?<winner>$USERNAME)\\w+beat\\w+@(?<loser>$USERNAME)")
    }
}
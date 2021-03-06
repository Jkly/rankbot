package jkly.rankbot.messagehandler

import jkly.rankbot.PlayerRepository
import jkly.rankbot.SlackPlayer
import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.elo.Match
import jkly.rankbot.elo.Player
import jkly.slack.SlackClient
import jkly.slack.UserListResponse
import jkly.slack.rtm.MessageEventHandler
import jkly.slack.rtm.MessageSender
import jkly.slack.rtm.event.MessageEvent
import java.util.regex.Pattern

class ReportMatchResult (val client: SlackClient, val eloCalculator: EloCalculator,
                         val playerRepository: PlayerRepository) : MessageEventHandler() {
    override fun handle(event: MessageEvent, sender: MessageSender) {
        val matcher = PATTERN.matcher(event.text)
        if (matcher.matches()) {
            val userList = client.userList()

            val winnerId = matcher.group("winner")
            val loserId = matcher.group("loser")

            val winner = userList.getPlayer(winnerId)
            val loser = userList.getPlayer(loserId)

            val (updatedWinner, updatedLoser) = eloCalculator.updateRatings(playMatch(winner.player, loser.player))

            playerRepository.save(winner.copy(player = updatedWinner))
            playerRepository.save(loser.copy(player = updatedLoser))

            sender.send(event.channel,
                    "${playerSummaryMessage(winnerId, winner, updatedWinner)}\n" +
                            "${playerSummaryMessage(loserId, loser, updatedLoser)}")
        }
    }

    private fun playerSummaryMessage(name: String, start:SlackPlayer, updatePlayer:Player) : String {
        val ratingChange = updatePlayer.rating - start.player.rating
        val ratingChangeEmoji = if (ratingChange < 0) {
            ":arrow_lower_right:"
        } else if (ratingChange > -0.01 && ratingChange < 0.01) {
            ":arrow_right:"
        } else {
            ":arrow_upper_right:"
        }
        return "<@${start.slackId}|$name> - ${updatePlayer.rating.format()} - ${updatePlayer.gamesPlayed} games - $ratingChangeEmoji ${ratingChange.format()}"
    }

    private fun Double.format() = java.lang.String.format("%.${2}f", this)

    private fun playMatch(winner: Player, loser: Player): Match {
        return Match(winner.updateGamesPlayed(), loser.updateGamesPlayed())
    }

    private fun Player.updateGamesPlayed(): Player {
        return this.copy(gamesPlayed = this.gamesPlayed+1)
    }

    private fun UserListResponse.getPlayer(userId: String): SlackPlayer {
        val id = this.members
                .map { it.id }
                .filter { it == userId }
                .first()
        return playerRepository.get(id)
    }

    companion object {
        val PATTERN: Pattern = Pattern.compile("<@(?<winner>$USER_ID)>\\s+beat\\s+<@(?<loser>$USER_ID)>")
    }
}
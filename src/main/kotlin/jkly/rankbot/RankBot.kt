package jkly.rankbot

import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.messagehandler.GetRankings
import jkly.rankbot.messagehandler.ReportMatchResult
import jkly.slack.SlackClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RankBot @Autowired constructor(val client: SlackClient,
                                     val eloCalculator: EloCalculator,
                                     val playerRepository: PlayerRepository) {

    fun run() {
        val session = client.rtmStart()
        session.connect(
                ReportMatchResult(client, eloCalculator, playerRepository),
                GetRankings(playerRepository))

    }

}
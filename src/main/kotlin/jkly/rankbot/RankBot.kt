package jkly.rankbot

import jkly.rankbot.elo.EloCalculator
import jkly.rankbot.messagehandler.GetRankings
import jkly.rankbot.messagehandler.ReportMatchResult
import jkly.slack.SlackClient
import jkly.slack.rtm.RtmSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
open class RankBot @Autowired constructor(val client: SlackClient,
                                     val eloCalculator: EloCalculator,
                                     val playerRepository: PlayerRepository) {

    lateinit var session : RtmSession

    @PostConstruct
    fun run() {
        session = client.rtmStart()
        session.connect(
                ReportMatchResult(client, eloCalculator, playerRepository),
                GetRankings(playerRepository))
    }

    @PreDestroy
    fun stop() {
        session.stop()
    }

}
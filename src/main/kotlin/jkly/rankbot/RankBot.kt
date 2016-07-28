package jkly.rankbot

import jkly.rankbot.slack.SlackClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RankBot @Autowired constructor(val client: SlackClient) {

    fun run() {
        val session = client.rtmStart()
        session.connect()

    }

}
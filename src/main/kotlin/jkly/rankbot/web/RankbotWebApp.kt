package jkly.rankbot.web

import jkly.rankbot.elo.EloCalculator
import jkly.slack.SlackClient
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class RankbotWebApp {
    @Bean
    open fun eloCalculator() = EloCalculator()

    @Bean
    open fun httpClient() = OkHttpClient()

    @Bean
    open fun slackClient() = SlackClient("load from config", httpClient())
}
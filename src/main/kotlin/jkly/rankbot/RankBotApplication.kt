package jkly.rankbot

import jkly.rankbot.elo.EloCalculator
import jkly.slack.SlackClient
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties
open class RankBotApplication {
    @Autowired
    lateinit var configuration: RankBotConfiguration

    @Bean
    open fun eloCalculator() = EloCalculator()

    @Bean
    open fun httpClient() = OkHttpClient()

    @Bean
    open fun slackClient() = SlackClient(configuration.botToken, httpClient())

}
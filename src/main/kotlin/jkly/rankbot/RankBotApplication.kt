package jkly.rankbot

import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import jkly.rankbot.elo.EloCalculator
import jkly.slack.SlackClient
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit

@SpringBootApplication
@EnableConfigurationProperties
open class RankBotApplication {
    @Autowired
    lateinit var configuration: RankBotConfiguration

    @Bean
    open fun eloCalculator() = EloCalculator()

    @Bean
    open fun httpClient() = OkHttpClient.Builder().readTimeout(0, TimeUnit.NANOSECONDS).build()

    @Bean
    open fun slackClient() = SlackClient(configuration.botToken, httpClient())

    @Bean
    open fun entityStore(config: XodusConfiguration) : PersistentEntityStore {
        return PersistentEntityStores.newInstance(config.dataDir)
    }
}
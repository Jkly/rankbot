package jkly.rankbot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "rankbot")
open class RankBotConfiguration() {
    lateinit var botToken: String
        get
        set
}
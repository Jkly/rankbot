package jkly.rankbot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "rankbot")
data class RankBotConfiguration(val botToken : String = "")
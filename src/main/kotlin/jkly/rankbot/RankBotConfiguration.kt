package jkly.rankbot

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "rankbot")
data class RankBotConfiguration(val botToken : String = "")
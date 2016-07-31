package jkly.rankbot.web

import jkly.rankbot.elo.EloCalculator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class RankbotWebApp {
    @Bean
    open fun eloCalculator() = EloCalculator()
}
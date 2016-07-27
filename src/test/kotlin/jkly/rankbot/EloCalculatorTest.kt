package jkly.rankbot

import io.kotlintest.specs.ShouldSpec

class EloCalculatorTest : ShouldSpec() {
    override val oneInstancePerTest = true

    init {
        "ELO calculator" {
            val elo = EloCalculator(kConstant = 32.0, minGames = 1)

            should("increase rating after beating a player with lower rating") {
                val updated = elo.updateRatings(Match(Player(2400.0, 1), Player(2000.0, 1)))
                updated.winner.rating shouldBe(2403.0).plusOrMinus(0.1)
                updated.loser.rating shouldBe(1997.0).plusOrMinus(0.1)
            }

            should("decrease rating after losing to a player with lower rating") {
                val updated = elo.updateRatings(Match(Player(2000.0, 1), Player(2400.0, 1)))
                updated.winner.rating shouldBe(2029.0).plusOrMinus(0.1)
                updated.loser.rating shouldBe(2371.0).plusOrMinus(0.1)
            }

            should("anchor loser to a min rating") {
                val updated = elo.updateRatings(Match(Player(16.0, 1), Player(16.0, 1)))
                updated.loser.rating shouldBe(16.0).plusOrMinus(0.1)
            }
        }

        "ELO calculator with min games" {
            val elo = EloCalculator(minGames = 5)

            should("give default rating") {
                val updated = elo.updateRatings(Match(Player(1500.0, 4), Player(1500.0, 4)))
                updated.winner.rating shouldBe(1500.0).plusOrMinus(0.1)
                updated.loser.rating shouldBe(1500.0).plusOrMinus(0.1)
            }
        }
    }
}
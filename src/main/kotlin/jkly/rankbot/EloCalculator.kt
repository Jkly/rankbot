package jkly.rankbot

class EloCalculator(val defaultRating:Double = 1500.0, val kConstant: Double = 800.0, val minElo : Double = 16.0) {

    fun updateRatings(match: Match) : Match {
        return match.copy(
                winner = match.winner.updatePlayer(match.loser, Result.WIN),
                loser = match.loser.updatePlayer(match.winner, Result.LOST))
    }

    private fun Player.updatePlayer(opponent: Player, result: Result): Player {
        val newRating: Double
        if (this.gamesPlayed < 5) {
            newRating = defaultRating
        } else {
            newRating = Math.min(this.rating +
                    kFactor() * (result.constant - winProbablility(opponent.rating)), minElo)
        }
        return this.copy(rating = newRating)
    }

    private fun Player.winProbablility(opponentRating:Double): Double {
        return 1 / (1 + Math.pow(10.0, (opponentRating-this.rating)/400))
    }

    private fun Player.kFactor() = kConstant / this.gamesPlayed

    enum class Result(val constant: Double) {
        WIN(1.0),
        LOST(0.0)
    }
}


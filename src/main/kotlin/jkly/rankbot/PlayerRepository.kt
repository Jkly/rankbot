package jkly.rankbot

import jkly.rankbot.elo.Player

interface PlayerRepository {

    fun get(id:String):Player
}
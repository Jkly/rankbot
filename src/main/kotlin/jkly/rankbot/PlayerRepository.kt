package jkly.rankbot

interface PlayerRepository {

    fun get(id:String):SlackPlayer
    fun save(player:SlackPlayer)
}
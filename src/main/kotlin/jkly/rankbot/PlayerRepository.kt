package jkly.rankbot

interface PlayerRepository {

    fun get(id:String):SlackPlayer
    fun save(slackPlayer:SlackPlayer)
}
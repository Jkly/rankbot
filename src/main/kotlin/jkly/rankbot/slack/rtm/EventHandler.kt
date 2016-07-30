package jkly.rankbot.slack.rtm

interface EventHandler {

    fun accept(type:String):Boolean

    fun handle(json: String, sender: MessageSender)
}
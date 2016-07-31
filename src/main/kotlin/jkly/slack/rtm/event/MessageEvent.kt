package jkly.slack.rtm.event

data class MessageEvent(val channel: String, val user: String, val text:String, val ts: String)

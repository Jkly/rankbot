package jkly.rankbot.slack

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SlackClient(val token: String) {
    val client = OkHttpClient()
    val jsonMapper = ObjectMapper()

    fun rtmStart() {
        val url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegment("rtm.start")
                .addQueryParameter("token", token).build()
        val request = Request.Builder().get().url(url).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        val body = response.body().string()
        println(body)
        jsonMapper.readValue(body, SlackResponse::class.java)


    }

    companion object {
        val BASE_URL = "https://slack.com/api"
    }
}
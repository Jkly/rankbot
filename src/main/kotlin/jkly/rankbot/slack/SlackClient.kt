package jkly.rankbot.slack

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SlackClient(val token: String) {
    val client = OkHttpClient()

    fun rtmStart() {
        val url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegment("rtm.start")
                .addQueryParameter("token", token).build()
        val request = Request.Builder().get().url(url).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        println(response.body().string())


    }

    companion object {
        val BASE_URL = "https://slack.com/api"
    }
}
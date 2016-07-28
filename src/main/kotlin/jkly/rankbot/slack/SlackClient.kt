package jkly.rankbot.slack

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SlackClient(val token: String) {
    val client = OkHttpClient()
    val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

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
        val json = gson.fromJson(body, RtmStartResponse::class.java)

        println(json.ok)
        println(json.url)

    }

    companion object {
        val BASE_URL = "https://slack.com/api"
    }
}
package jkly.rankbot.slack

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.IOException

class SlackClient(val token: String) {
    val client = OkHttpClient()
    val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    fun rtmStart(): RtmSession {
        val url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegment("rtm.start")
                .addQueryParameter("token", token).build()
        val request = Request.Builder().get().url(url).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException("Unexpected code $response")
        }

        val body = response.body().string()
        LOGGER.debug("rtm.start response: $body")
        val slackResponse = gson.fromJson(body, SlackResponse::class.java)

        if (slackResponse.ok) {
            val rtmStartResponse = gson.fromJson(body, RtmStartResponse::class.java)
            return RtmSession(rtmStartResponse.url)
        } else {
            val errorResponse: ErrorResponse?
            try {
                errorResponse = gson.fromJson(body, ErrorResponse::class.java)
            } catch (e: Exception) {
                throw RuntimeException("Failed to start Real Time Messaging session. " +
                        "Could not parse reason from response: $body.", e)
            }
            throw RuntimeException("Failed to start Real Time Messaging session: ${errorResponse.error}")
        }
    }

    companion object {
        val BASE_URL = "https://slack.com/api"
        val LOGGER = LoggerFactory.getLogger(SlackClient::class.java)
    }
}
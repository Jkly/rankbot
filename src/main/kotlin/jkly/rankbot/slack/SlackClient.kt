package jkly.rankbot.slack

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jkly.rankbot.slack.rtm.RtmSession
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SlackClient @Autowired constructor(val token: String, val client:OkHttpClient = OkHttpClient()) {
    val gson: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    fun rtmStart(): RtmSession {
        val url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegment("rtm.start")
                .addQueryParameter("token", token).build()
        val request = Request.Builder().get().url(url).build()
        return apiCall(request) { body ->
            val rtmStartResponse = gson.fromJson(body, RtmStartResponse::class.java)
            RtmSession(client, rtmStartResponse.url)
        }
    }

    fun userList(): UserListResponse {
        val url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegment("users.list")
                .addQueryParameter("token", token).build()
        val request = Request.Builder().get().url(url).build()

        return apiCall(request) { body ->
            gson.fromJson(body, UserListResponse::class.java)
        }
    }

    private fun <T> apiCall(request: Request, responseHandler:(String) -> T): T {
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException("Unexpected code $response")
        }

        val body = response.body().string()
        LOGGER.debug("${request.url().encodedPath()} response: $body")
        val slackResponse = gson.fromJson(body, SlackResponse::class.java)

        if (slackResponse.ok) {
            return responseHandler.invoke(body)
        } else {
            val errorResponse: ErrorResponse?
            try {
                errorResponse = gson.fromJson(body, ErrorResponse::class.java)
            } catch (e: Exception) {
                throw RuntimeException("Failed to execute api call ${request.url().encodedPath()}. " +
                        "Could not parse reason from response: $body.", e)
            }
            throw RuntimeException("Failed to execute api call ${request.url().encodedPath()}: ${errorResponse.error}")
        }
    }

    companion object {
        val BASE_URL = "https://slack.com/api"
        val LOGGER: Logger = LoggerFactory.getLogger(SlackClient::class.java)
    }
}
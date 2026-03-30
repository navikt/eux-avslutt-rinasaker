package no.nav.eux.avslutt.rinasaker.webapp.mock

import okhttp3.mockwebserver.MockResponse
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE

fun slackWebhookResponse() =
    MockResponse().apply {
        setResponseCode(200)
        setHeader(CONTENT_TYPE, TEXT_PLAIN_VALUE)
        setBody("ok")
    }

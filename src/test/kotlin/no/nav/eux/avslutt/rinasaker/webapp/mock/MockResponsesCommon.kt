package no.nav.eux.avslutt.rinasaker.webapp.mock

import okhttp3.mockwebserver.MockResponse

fun response204() =
    MockResponse().apply {
        setResponseCode(204)
    }

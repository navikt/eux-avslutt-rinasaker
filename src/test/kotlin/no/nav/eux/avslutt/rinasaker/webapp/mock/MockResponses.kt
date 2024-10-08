package no.nav.eux.avslutt.rinasaker.webapp.mock

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.TEXT_PLAIN

fun mockResponse(request: RecordedRequest) =
    when (request.method) {
        HttpMethod.POST.name() -> mockResponsePost(request)
        HttpMethod.GET.name() -> mockResponseGet(request)
        HttpMethod.PATCH.name() -> mockResponsePatch(request)
        HttpMethod.DELETE.name() -> mockResponseDelete(request)
        else -> defaultResponse()
    }

fun mockResponsePost(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/oauth2/v2.0/token" -> tokenResponse()
        "/api/v1/journalposter/settStatusAvbryt" -> response204()
        "/api/v1/rinasaker/1/avsluttGlobalt" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/5/avsluttLokalt" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/8/avsluttLokalt" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/9/avsluttGlobalt" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/1/arkiver" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/5/arkiver" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/8/arkiver" -> getEuxRinaTerminatorApiNoContent()
        "/api/v1/rinasaker/9/arkiver" -> getEuxRinaTerminatorApiNoContent()
        else -> defaultResponse()
    }

fun mockResponsePatch(request: RecordedRequest) =
    when (request.uriEndsWith) {
        else -> defaultResponse()
    }

fun mockResponseGet(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/api/v1/rinasaker/1/status" -> getEuxRinaTerminatorApiStatusResponseTrue()
        "/api/v1/rinasaker/2/status" -> getEuxRinaTerminatorApiStatusResponseTrue()
        "/api/v1/rinasaker/3/status" -> getEuxRinaTerminatorApiStatusResponseTrue()
        "/api/v1/rinasaker/4/status" -> getEuxRinaTerminatorApiStatusResponseTrue()
        "/api/v1/rinasaker/5/status" -> getEuxRinaTerminatorApiStatusResponseFalse()
        else -> defaultResponse()
    }

fun mockResponseDelete(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/api/v1/rinasaker/3" -> response204()
        else -> defaultResponse()
    }

fun defaultResponse() =
    MockResponse().apply {
        setHeader(CONTENT_TYPE, TEXT_PLAIN)
        setBody("no mock defined")
        setResponseCode(500)
    }

val RecordedRequest.uriEndsWith get() = requestUrl.toString().split("/mock")[1]

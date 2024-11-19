package no.nav.eux.avslutt.rinasaker.integration

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.exception.HandlingManglerException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class EuxRinaTerminatorApiClient(
    @Value("\${endpoint.eux-rina-terminator-api}")
    val euxRinaTerminatorApiEndpoint: String,
    val euxRinaTerminatorApiRestTemplate: RestTemplate
) {

    val log = logger {}

    fun avsluttGlobalt(rinasakId: Int) = tryHandling {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/avsluttGlobalt")
            .retrieve()
            .toBodilessEntity()
        log.info { "Avsluttet $rinasakId globalt" }
    }

    fun avsluttLokalt(rinasakId: Int) = tryHandling {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/avsluttLokalt")
            .retrieve()
            .toBodilessEntity()
        log.info { "Avsluttet $rinasakId lokalt" }
    }

    fun arkiver(rinasakId: Int) = tryHandling {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/arkiver")
            .retrieve()
            .toBodilessEntity()
        log.info { "Arkiverte rinasak $rinasakId" }
    }

    fun slettDokumentutkast(rinasakId: Int) = tryHandling {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/slettDokumentutkast")
            .retrieve()
            .toBodilessEntity()
        log.info { "Slettet dokumentutkast i rinasak $rinasakId" }
    }

    fun tryHandling(action: () -> Unit) {
        try {
            action()
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == CONFLICT) {
                val error = e.getResponseBodyAs(ConflictError::class.java)
                throw HandlingManglerException(error?.message ?: "Handling mangler")
            }
        }
    }

    data class ConflictError(
        val message: String,
        val conflictCategory: String
    )
}

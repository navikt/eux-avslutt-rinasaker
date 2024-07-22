package no.nav.eux.avslutt.rinasaker.integration

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class EuxRinaTerminatorApiClient(
    @Value("\${endpoint.eux-rina-terminator-api}")
    val euxRinaTerminatorApiEndpoint: String,
    val euxRinaTerminatorApiRestTemplate: RestTemplate
) {

    val log = logger {}

    fun avsluttGlobalt(rinasakId: Int) {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/avsluttGlobalt")
            .retrieve()
            .toBodilessEntity()
        log.info { "Avsluttet $rinasakId globalt" }
    }

    fun avsluttLokalt(rinasakId: Int) {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/avsluttLokalt")
            .retrieve()
            .toBodilessEntity()
        log.info { "Avsluttet $rinasakId lokalt" }
    }

    fun arkiver(rinasakId: Int) {
        euxRinaTerminatorApiRestTemplate
            .post()
            .uri("${euxRinaTerminatorApiEndpoint}/api/v1/rinasaker/$rinasakId/arkiver")
            .retrieve()
            .toBodilessEntity()
        log.info { "Arkiverte rinasak $rinasakId" }
    }
}

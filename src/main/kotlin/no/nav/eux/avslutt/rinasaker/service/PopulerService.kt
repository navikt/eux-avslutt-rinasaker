package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.NY_SAK
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import java.util.*

@Service
class PopulerService(
    val repository: RinasakRepository
) {

    val log = logger {}

    fun leggTilRinasak(
        rinasakId: Int,
        bucType: String,
    ) {
        val status = repository
            .findByRinasakId(rinasakId)
            ?.copy(endretTidspunkt = now())
            ?: rinasak(rinasakId, bucType, NY_SAK)
        repository.save(status)
        log.info { "Sak oppdatert eller lagt til" }
    }

    fun leggTilDokument() {

    }
}

fun rinasak(
    rinasakId: Int,
    bucType: String,
    status: Rinasak.Status
) = Rinasak(
    rinasakStatusUuid = UUID.randomUUID(),
    rinasakId = rinasakId,
    status = status,
    bucType = bucType
)

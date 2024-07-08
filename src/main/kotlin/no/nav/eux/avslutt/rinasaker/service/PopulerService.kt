package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.NY_SAK
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import java.util.*

@Service
class PopulerService(
    val rinasakRepository: RinasakRepository,
    val dokumentRepository: DokumentRepository,
) {

    val log = logger {}

    fun leggTilRinasak(
        rinasakId: Int,
        bucType: String,
    ) {
        val status = rinasakRepository
            .findByRinasakId(rinasakId)
            ?.copy(endretTidspunkt = now())
            ?: rinasak(rinasakId, bucType, NY_SAK)
        rinasakRepository.save(status)
        log.info { "Sak oppdatert eller lagt til" }
    }

    fun leggTilDokument(
        rinasakId: Int,
        sedId: UUID,
        sedVersjon: Int,
        sedType: String,
    ) {
        val dokument = dokumentRepository
            .findBySedIdAndSedVersjon(sedId, sedVersjon)
            ?.copy(endretTidspunkt = now())
            ?: dokument(rinasakId, sedId, sedVersjon, sedType)
        dokumentRepository.save(dokument)
        log.info { "Dokument oppdatert eller lagt til" }
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

fun dokument(
    rinasakId: Int,
    sedId: UUID,
    sedVersjon: Int,
    sedType: String
) = Dokument(
    dokumentUuid = UUID.randomUUID(),
    rinasakId = rinasakId,
    sedId = sedId,
    sedVersjon = sedVersjon,
    sedType = sedType
)
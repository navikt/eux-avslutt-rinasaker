package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.NY_SAK
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
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
        erSakseier: Boolean,
    ) {
        val rinasak = rinasakRepository
            .findByRinasakId(rinasakId)
            ?.copy(endretTidspunkt = now())
            ?: rinasak(rinasakId, bucType, erSakseier, NY_SAK)
        rinasakRepository.save(rinasak)
        log.info { "Sak oppdatert eller lagt til" }
    }

    fun leggTilDokument(
        rinasakId: Int,
        sedId: UUID,
        sedVersjon: Int,
        sedType: String,
        status: Dokument.Status,
    ) {
        val dokument = dokumentRepository
            .findBySedIdAndSedVersjon(sedId, sedVersjon)
            ?.copy(endretTidspunkt = now())
            ?: dokument(rinasakId, sedId, sedVersjon, sedType, status)
        dokumentRepository.save(dokument)
        log.info { "Dokument oppdatert eller lagt til" }
        settVirksom(rinasakId)
    }

    fun settVirksom(rinasakId: Int) {
        rinasakRepository
            .findByRinasakId(rinasakId)
            ?.takeIf { it.status == UVIRKSOM }
            ?.let {
                val oppdatertRinasak = it.copy(
                    status = NY_SAK,
                    endretBruker = "sett-virksom",
                    endretTidspunkt = now()
                )
                rinasakRepository.save(oppdatertRinasak)
                log.info { "Sak satt til virksom" }
            }
    }
}

fun rinasak(
    rinasakId: Int,
    bucType: String,
    erSakseier: Boolean,
    status: Rinasak.Status
) = Rinasak(
    rinasakStatusUuid = UUID.randomUUID(),
    rinasakId = rinasakId,
    status = status,
    bucType = bucType,
    erSakseier = erSakseier
)

fun dokument(
    rinasakId: Int,
    sedId: UUID,
    sedVersjon: Int,
    sedType: String,
    status: Dokument.Status
) = Dokument(
    dokumentUuid = UUID.randomUUID(),
    rinasakId = rinasakId,
    sedId = sedId,
    sedVersjon = sedVersjon,
    sedType = sedType,
    status = status
)

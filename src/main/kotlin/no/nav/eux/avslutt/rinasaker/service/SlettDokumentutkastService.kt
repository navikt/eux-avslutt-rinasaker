package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.integration.EuxRinaTerminatorApiClient
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.SLETT_DOKUMENTUTKAST
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.logging.mdc
import org.springframework.stereotype.Service

@Service
class SlettDokumentutkastService(
    val rinasakRepository: RinasakRepository,
    val handlingService: HandlingService,
    val euxRinaTerminatorApiClient: EuxRinaTerminatorApiClient
) {

    val log = logger {}

    fun slettDokumentutkast() {
        bucList.forEach { it.slettDokumentutkast() }
    }

    fun Buc.slettDokumentutkast() {
        mdc(bucType = navn)
        rinasakRepository
            .findAllByStatusAndBucType(SLETT_DOKUMENTUTKAST, navn)
            .also { log.info { "${it.size} saker vil få slettet dokumentutkast for X001 for buc type $navn" } }
            .forEach { it.trySlettDokumentutkast() }
    }

    fun Rinasak.trySlettDokumentutkast() =
        handlingService.tryHandling(
            rinasak = this,
            tilStatus = UVIRKSOM,
            endretBruker = "slett-dokumentutkast"
        ) {
            euxRinaTerminatorApiClient.slettDokumentutkast(rinasakId)
        }

}

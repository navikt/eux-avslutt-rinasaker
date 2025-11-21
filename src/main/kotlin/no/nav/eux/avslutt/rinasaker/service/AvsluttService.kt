package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.integration.EuxRinaTerminatorApiClient
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.buc.pBucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.logging.mdc
import org.springframework.stereotype.Service

@Service
class AvsluttService(
    val rinasakRepository: RinasakRepository,
    val handlingService: HandlingService,
    val euxRinaTerminatorApiClient: EuxRinaTerminatorApiClient,
) {

    val log = logger {}

    fun avsluttRinasaker() {
        (bucList + pBucList)
            .forEach { it.avslutt() }
    }

    fun Buc.avslutt() {
        mdc(bucType = navn)
        rinasakRepository
            .findAllByStatusAndBucType(TIL_AVSLUTNING_LOKALT, navn)
            .take(2000)
            .also { log.info { "${it.size} saker vil bli avsluttet lokalt for buc type $navn" } }
            .forEach { it.tryAvsluttLokalt() }
        rinasakRepository
            .findAllByStatusAndBucType(TIL_AVSLUTNING_GLOBALT, navn)
            .take(1000)
            .also { log.info { "${it.size} saker vil bli avsluttet globalt for buc type $navn" } }
            .forEach { it.tryAvsluttGlobalt() }
    }

    fun Rinasak.tryAvsluttGlobalt() =
        handlingService.tryHandling(
            rinasak = this,
            tilStatus = AVSLUTTET_GLOBALT,
            endretBruker = "avslutt"
        ) {
            euxRinaTerminatorApiClient.avsluttGlobalt(rinasakId)
        }

    fun Rinasak.tryAvsluttLokalt() =
        handlingService.tryHandling(
            rinasak = this,
            tilStatus = AVSLUTTET_LOKALT,
            endretBruker = "avslutt"
        ) {
            euxRinaTerminatorApiClient.avsluttLokalt(rinasakId)
        }

}

package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.integration.EuxRinaTerminatorApiClient
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.logging.mdc
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class AvsluttService(
    val rinasakRepository: RinasakRepository,
    val handlingService: HandlingService,
    val euxRinaTerminatorApiClient: EuxRinaTerminatorApiClient,
) {

    val log = logger {}

    fun avsluttRinasaker() {
        bucList.forEach { it.avslutt() }
    }

    fun Buc.avslutt() {
        mdc(bucType = navn)
        rinasakRepository
            .findAllByStatusAndBucType(TIL_AVSLUTNING_LOKALT, navn)
            .take(1000)
            .also { log.info { "${it.size} saker vil bli avsluttet lokalt for buc type $navn" } }
            .forEach { it.tryAvsluttLokalt() }
        rinasakRepository
            .findAllByStatusAndBucType(TIL_AVSLUTNING_GLOBALT, navn)
            .take(1000)
            .also { log.info { "${it.size} saker vil bli avsluttet globalt for buc type $navn" } }
            .forEach { it.tryAvsluttGlobalt() }
    }

    fun Rinasak.tryAvsluttGlobalt(): Rinasak =
        if (erAvsluttetIRina())
            alleredeAvsluttet()
        else
            handlingService.tryHandling(
                rinasak = this,
                tilStatus = AVSLUTTET_GLOBALT,
                endretBruker = "avslutt"
            ) {
                euxRinaTerminatorApiClient.avsluttGlobalt(rinasakId)
            }

    fun Rinasak.tryAvsluttLokalt(): Rinasak =
        if (erAvsluttetIRina())
            alleredeAvsluttet()
        else
            handlingService.tryHandling(
                rinasak = this,
                tilStatus = AVSLUTTET_LOKALT,
                endretBruker = "avslutt"
            ) {
                euxRinaTerminatorApiClient.avsluttLokalt(rinasakId)
            }

    fun Rinasak.erAvsluttetIRina(): Boolean =
        try {
            euxRinaTerminatorApiClient.erAvsluttet(rinasakId)
        } catch (e: Exception) {
            mdc(rinasakId = rinasakId, bucType = bucType)
            log.warn(e) { "Kunne ikke sjekke om rinasak er avsluttet i RINA, forsøker avslutning" }
            false
        }

    fun Rinasak.alleredeAvsluttet(): Rinasak {
        mdc(rinasakId = rinasakId, bucType = bucType)
        log.info { "Rinasak allerede avsluttet i RINA" }
        return rinasakRepository.save(
            copy(
                status = ALLEREDE_AVSLUTTET,
                endretBruker = "avslutt",
                endretTidspunkt = now()
            )
        )
    }

}

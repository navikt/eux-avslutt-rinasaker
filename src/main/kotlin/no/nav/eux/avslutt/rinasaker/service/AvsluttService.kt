package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.integration.EuxRinaTerminatorApiClient
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class AvsluttService(
    val rinasakRepository: RinasakRepository,
    val euxRinaTerminatorApiClient: EuxRinaTerminatorApiClient
) {

    val log = logger {}

    fun avsluttRinasaker() {
        bucList.forEach { it.avslutt() }
    }

    fun Buc.avslutt() {
        mdc(bucType = navn)
        rinasakRepository
            .findAllByStatusAndBucType(TIL_AVSLUTNING_LOKALT, navn)
            .also { log.info { "${it.size} saker vil bli avsluttet lokalt for buc type $navn" } }
            .forEach { it.avsluttLokalt() }
        rinasakRepository
            .findAllByStatusAndBucType(TIL_AVSLUTNING_GLOBALT, navn)
            .also { log.info { "${it.size} saker vil bli avsluttet globalt for buc type $navn" } }
            .forEach { it.avsluttGlobalt() }
    }

    fun Rinasak.avsluttGlobalt() {
        mdc(rinasakId = rinasakId)
        euxRinaTerminatorApiClient.avsluttGlobalt(rinasakId)
        rinasakRepository.save(
            copy(
                status = AVSLUTTET_GLOBALT,
                endretBruker = "avslutt",
                endretTidspunkt = now()
            )
        )
    }

    fun Rinasak.avsluttLokalt() {
        mdc(rinasakId = rinasakId)
        euxRinaTerminatorApiClient.avsluttLokalt(rinasakId)
        rinasakRepository.save(
            copy(
                status = AVSLUTTET_LOKALT,
                endretBruker = "avslutt",
                endretTidspunkt = now()
            )
        )
    }

}

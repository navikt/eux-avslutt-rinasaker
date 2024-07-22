package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.integration.EuxRinaTerminatorApiClient
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.ARKIVERT
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.TIL_ARKIVERING
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository

import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class ArkiverService(
    val rinasakRepository: RinasakRepository,
    val euxRinaTerminatorApiClient: EuxRinaTerminatorApiClient
) {

    val log = logger {}

    fun arkiverRinasaker() {
        bucList.forEach { it.arkiver() }
    }

    fun Buc.arkiver() {
        mdc(bucType = navn)
        rinasakRepository
            .findAllByStatusAndBucType(TIL_ARKIVERING, navn)
            .also { log.info { "${it.size} saker vil bli arkivert for buc type $navn" } }
            .forEach { it.arkiver() }
    }

    fun Rinasak.arkiver() {
        mdc(rinasakId = rinasakId)
        euxRinaTerminatorApiClient.arkiver(rinasakId)
        rinasakRepository.save(
            copy(
                status = ARKIVERT,
                endretBruker = "arkiver",
                endretTidspunkt = now()
            )
        )
    }

}

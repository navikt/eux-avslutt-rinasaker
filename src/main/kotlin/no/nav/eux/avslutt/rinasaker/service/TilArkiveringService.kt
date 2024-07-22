package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class TilArkiveringService(
    val rinasakRepository: RinasakRepository
) {

    val log = logger {}

    fun settRinasakerTilArkivering() {
        bucList.forEach { it.settTilArkivering() }
    }

    fun Buc.settTilArkivering() {
        mdc(bucType = navn)
        val sakerAvsluttetGlobalt = rinasakRepository
            .findAllByStatusAndBucTypeAndEndretTidspunktBefore(
                status = AVSLUTTET_GLOBALT,
                bucType = navn,
                endretTidspunkt = now().minusDays(antallDagerBeforeArkivering)
            )
        log.info { "${sakerAvsluttetGlobalt.size} globalt avsluttede saker funnet for buc type $navn for arkivering" }
        sakerAvsluttetGlobalt.forEach { it.tilArkivering() }
        val sakerAvsluttetLokalt = rinasakRepository
            .findAllByStatusAndBucTypeAndEndretTidspunktBefore(
                status = AVSLUTTET_LOKALT,
                bucType = navn,
                endretTidspunkt = now().minusDays(antallDagerBeforeArkivering)
            )
        log.info { "${sakerAvsluttetLokalt.size} lokalt avsluttede saker funnet for buc type $navn for arkivering" }
        sakerAvsluttetLokalt.forEach { it.tilArkivering() }
    }

    fun Rinasak.tilArkivering() {
        mdc(rinasakId = rinasakId)
        rinasakRepository.save(
            copy(
                status = TIL_ARKIVERING,
                endretBruker = "til-arkivering",
                endretTidspunkt = now()
            )
        )
        log.info { "Rinasak satt til arkivering" }
    }
}

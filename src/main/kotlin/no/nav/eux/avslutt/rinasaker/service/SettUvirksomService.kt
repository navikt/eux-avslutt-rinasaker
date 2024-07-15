package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.NY_SAK
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class SettUvirksomService(
    val rinasakRepository: RinasakRepository,
    val dokumentRepository: DokumentRepository,
) {

    val log = logger {}

    fun settRinasakerUvirksom() {
        bucList.forEach { it.settUvirksom() }
    }

    fun Buc.settUvirksom() {
        mdc(bucType = navn)
        val date = now().minusDays(antallDagerBeforeUvirksom)
        val uvirksommeDokumenter = dokumentRepository.findDokumenterBeforeDate(date, NY_SAK)
        log.info { "$navn har ${uvirksommeDokumenter.size} uvirksomme saker" }
        uvirksommeDokumenter.forEach { it.settUvirksom() }
    }

    fun Dokument.settUvirksom() {
        mdc(
            rinasakId = rinasakId,
            sedId = sedId,
            sedVersjon = sedVersjon,
            sedType = sedType,
        )
        val rinasak = rinasakRepository
            .findByRinasakId(rinasakId)
            ?: throw RuntimeException("Rinasak med id $rinasakId ikke funnet")
        mdc(bucType = rinasak.bucType)
        val oppdatertRinasak = rinasak.copy(
            status = UVIRKSOM,
            endretBruker = "sett-uvirksom",
            endretTidspunkt = now()
        )
        rinasakRepository.save(oppdatertRinasak)
        log.info { "${rinasak.rinasakId} satt til uvirksom buc: ${rinasak.bucType}" }
    }
}

package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.BucAvsluttScope
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.AVSLUTTES_AV_MOTPART
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class TilAvslutningService(
    val rinasakRepository: RinasakRepository,
    val rinasakService: RinasakService,
) {

    val log = logger {}

    fun settRinasakerTilAvslutning() {
        bucList.forEach { it.settTilAvslutning() }
    }

    fun Buc.settTilAvslutning() {
        mdc(bucType = navn)
        val uvirksommeSaker = rinasakRepository.findAllByStatusAndBucType(UVIRKSOM, navn)
        log.info { "${uvirksommeSaker.size} uvirksomme saker funner for buc type $navn" }
        uvirksommeSaker
            .filter { kanAvsluttes(it) }
            .also { "${it.size} saker vil forsøkes avsluttes for buc type $navn" }
            .forEach { tilAvslutning(it) }
        uvirksommeSaker
            .filterNot { kanAvsluttes(it) }
            .also { "${it.size} saker vil avsluttes av motpart for buc type $navn" }
            .forEach { it.avsluttesAvMotpart() }
    }

    fun Buc.kanAvsluttes(rinasak: Rinasak): Boolean =
        if (kreverSakseier)
            rinasak.erSakseier
        else
            true

    fun Buc.tilAvslutning(rinasak: Rinasak) {
        mdc(
            rinasakId = rinasak.rinasakId,
            bucType = navn,
        )
        if (rinasakService.sisteSedType(rinasak.rinasakId) == sisteSedForAvslutningAutomatisk)
            rinasak.avslutt(bucAvsluttScope)
        else
            rinasak.lagOppgave()
    }

    fun Rinasak.lagOppgave() {
        rinasakRepository.save(
            copy(
                status = Rinasak.Status.OPPRETT_OPPGAVE,
                endretBruker = "til-avslutning",
                endretTidspunkt = now()
            )
        )
        log.info { "Oppgave vil bli opprettet for rinasak" }
    }

    fun Rinasak.avslutt(bucAvsluttScope: BucAvsluttScope) {
        rinasakRepository.save(
            copy(
                status = bucAvsluttScope.tilAvslutningStatus,
                endretBruker = "til-avslutning",
                endretTidspunkt = now()
            )
        )
        log.info { "Rinasak satt til avslutning: ${bucAvsluttScope.tilAvslutningStatus}" }
    }

    fun Rinasak.avsluttesAvMotpart() {
        mdc(
            rinasakId = rinasakId,
            bucType = bucType,
        )
        rinasakRepository.save(
            copy(
                status = AVSLUTTES_AV_MOTPART,
                endretBruker = "til-avslutning",
                endretTidspunkt = now()
            )
        )
        log.info { "Rinasak vil avsluttes av motpart" }
    }

}

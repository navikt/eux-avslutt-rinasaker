package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.buc.Buc
import no.nav.eux.avslutt.rinasaker.model.buc.BucAvsluttScope
import no.nav.eux.avslutt.rinasaker.model.buc.bucList
import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.logging.mdc
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class TilAvslutningService(
    val rinasakRepository: RinasakRepository,
    val dokumentRepository: DokumentRepository,
) {

    val log = logger {}

    fun settRinasakerTilAvslutning() {
        bucList.forEach { it.settTilAvslutning() }
    }

    fun Buc.settTilAvslutning() {
        mdc(bucType = navn)
        rinasakRepository
            .findAllByStatusAndBucType(UVIRKSOM, navn)
            .also { "${it.size} saker vil forsøkes avsluttes for buc type $navn" }
            .forEach { tilAvslutning(it) }
    }

    fun Buc.tilAvslutning(rinasak: Rinasak) {
        mdc(
            rinasakId = rinasak.rinasakId,
            bucType = navn,
        )
        when {
            rinasak.erSakseier && bucAvsluttScopeSakseier != null -> tilAvslutning(rinasak, bucAvsluttScopeSakseier)
            !rinasak.erSakseier && bucAvsluttScopeMotpart != null -> tilAvslutning(rinasak, bucAvsluttScopeMotpart)
            else -> rinasak.avsluttesAvMotpart()
        }
    }

    fun Buc.tilAvslutning(rinasak: Rinasak, scope: BucAvsluttScope) {
        val dokumenter = rinasak.dokumenter()
        when {
            sisteSedFraNavAvslutning(dokumenter) -> rinasak avsluttMed scope
            sisteSedForAvslutning(dokumenter) -> rinasak avsluttMed scope
            sedExistsForAvslutning(dokumenter) -> rinasak avsluttMed scope
            mottattSedExistsForAvslutning(dokumenter) -> rinasak avsluttMed scope
            sentSedExistsForAvslutning(dokumenter) -> rinasak avsluttMed scope
            opprettOppgave -> rinasak.lagOppgave()
        }
    }

    fun Buc.sisteSedFraNavAvslutning(dokumenter: List<Dokument>) =
        sisteSedForAvslutningAutomatiskKrevesSendtFraNav
                && dokumenter.sisteSedSendtFraNav()
                && dokumenter.sisteSedType() in sisteSedForAvslutningAutomatisk

    fun Buc.sisteSedForAvslutning(dokumenter: List<Dokument>) =
        !sisteSedForAvslutningAutomatiskKrevesSendtFraNav
                && dokumenter.sisteSedType() in sisteSedForAvslutningAutomatisk

    fun Buc.sedExistsForAvslutning(dokumenter: List<Dokument>) =
        dokumenter.sedTyper().any { it in sedExistsForAvslutningAutomatisk }

    fun Buc.mottattSedExistsForAvslutning(dokumenter: List<Dokument>) =
        dokumenter.sedTyper().any { it in mottattSedExistsForAvslutningAutomatisk }

    fun Buc.sentSedExistsForAvslutning(dokumenter: List<Dokument>) =
        dokumenter.sedTyper().any { it in sentSedExistsForAvslutningAutomatisk }

    fun Rinasak.dokumenter() =
        dokumentRepository.findByRinasakId(rinasakId)

    fun List<Dokument>.sisteSedType() = maxByOrNull { it.endretTidspunkt }?.sedType

    fun List<Dokument>.sedTyper() = map { it.sedType }.distinct()

    fun List<Dokument>.sisteSedSendtFraNav() =
        maxByOrNull { it.endretTidspunkt }?.status == Dokument.Status.SENT

    fun Rinasak.lagOppgave() {
        mdc(rinasakId = rinasakId, bucType = bucType)
        rinasakRepository.save(
            copy(
                status = Rinasak.Status.OPPRETT_OPPGAVE,
                endretBruker = "til-avslutning",
                endretTidspunkt = now()
            )
        )
        log.info { "Oppgave vil bli opprettet for rinasak" }
    }

    infix fun Rinasak.avsluttMed(bucAvsluttScope: BucAvsluttScope) {
        mdc(rinasakId = rinasakId, bucType = bucType)
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
                status = Rinasak.Status.AVSLUTTES_AV_MOTPART,
                endretBruker = "til-avslutning",
                endretTidspunkt = now()
            )
        )
        log.info { "Rinasak vil avsluttes av motpart" }
    }

}

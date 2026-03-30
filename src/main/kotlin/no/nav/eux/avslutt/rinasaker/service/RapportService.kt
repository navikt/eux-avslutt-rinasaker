package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Service
class RapportService(
    val repository: RinasakRepository,
    val slackService: SlackService
) {

    val log = logger {}

    val numberFormat: NumberFormat = NumberFormat.getInstance(Locale.of("nb", "NO"))

    fun sendRapport() {
        val now = LocalDateTime.now()
        val forrigeMaanedStart = now.minusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay()
        val denneMaanedStart = now.withDayOfMonth(1).toLocalDate().atStartOfDay()
        val maanedNavn = forrigeMaanedStart.month
            .getDisplayName(TextStyle.FULL, Locale.of("nb", "NO"))
            .replaceFirstChar { it.titlecase() }
        val aar = forrigeMaanedStart.year
        val melding = buildString {
            append("*Månedlig rapport — Avslutt Rinasaker*\n")
            append("_${maanedNavn} ${aar}_\n")
            appendOppsummering(forrigeMaanedStart, denneMaanedStart)
            appendTopp3BucTyper(forrigeMaanedStart, denneMaanedStart)
            appendFeiledeSaker(forrigeMaanedStart, denneMaanedStart)
            appendNaaværendeStatus()
        }
        log.info { melding }
        slackService.post(melding)
    }

    private fun StringBuilder.appendOppsummering(
        fra: LocalDateTime,
        til: LocalDateTime
    ) {
        val nyeSaker = repository.countByOpprettetTidspunktGreaterThanEqualAndOpprettetTidspunktLessThan(fra, til)
        val avsluttetGlobalt = repository.countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(AVSLUTTET_GLOBALT, fra, til)
        val avsluttetLokalt = repository.countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(AVSLUTTET_LOKALT, fra, til)
        val arkivert = repository.countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(ARKIVERT, fra, til)
        val feilet = repository.countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(HANDLING_FEILET, fra, til) +
                repository.countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(HANDLING_MANGLER, fra, til)
        val avsluttesAvMotpart = repository.countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(AVSLUTTES_AV_MOTPART, fra, til)
        append("\n📊 *Oppsummering forrige måned:*\n")
        append("• Nye saker mottatt: `${fmt(nyeSaker)}`\n")
        append("• Avsluttet globalt: `${fmt(avsluttetGlobalt)}`\n")
        append("• Avsluttet lokalt: `${fmt(avsluttetLokalt)}`\n")
        append("• Arkivert: `${fmt(arkivert)}`\n")
        if (feilet > 0) append("• Feilet: `${fmt(feilet)}`\n")
        if (avsluttesAvMotpart > 0) append("• Avsluttes av motpart: `${fmt(avsluttesAvMotpart)}`\n")
    }

    private fun StringBuilder.appendTopp3BucTyper(
        fra: LocalDateTime,
        til: LocalDateTime
    ) {
        val avsluttetGlobaltSaker = repository.findAllByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(AVSLUTTET_GLOBALT, fra, til)
        val avsluttetLokaltSaker = repository.findAllByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(AVSLUTTET_LOKALT, fra, til)

        data class BucStats(val globalt: Long, val lokalt: Long) {
            val totalt get() = globalt + lokalt
        }

        val bucStats = mutableMapOf<String, BucStats>()
        avsluttetGlobaltSaker.groupBy { it.bucType }.forEach { (bucType, saker) ->
            bucStats[bucType] = BucStats(globalt = saker.size.toLong(), lokalt = 0)
        }
        avsluttetLokaltSaker.groupBy { it.bucType }.forEach { (bucType, saker) ->
            val existing = bucStats[bucType]
            bucStats[bucType] = BucStats(
                globalt = existing?.globalt ?: 0,
                lokalt = saker.size.toLong()
            )
        }

        if (bucStats.isEmpty()) return

        val sorted = bucStats.entries.sortedByDescending { it.value.totalt }
        val topp3 = sorted.take(3)
        val ovrige = sorted.drop(3).sumOf { it.value.totalt }

        append("\n📈 *Topp 3 BUC-typer — Avsluttet:*\n")
        topp3.forEach { (bucType, stats) ->
            append("• `$bucType`:  `${fmt(stats.globalt)}` globalt · `${fmt(stats.lokalt)}` lokalt\n")
        }
        if (ovrige > 0) {
            append("• _Øvrige: `${fmt(ovrige)}` avsluttet totalt_\n")
        }
    }

    private fun StringBuilder.appendFeiledeSaker(
        fra: LocalDateTime,
        til: LocalDateTime
    ) {
        val feiledeSaker = repository.findAllByStatusInAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(
            listOf(HANDLING_FEILET, HANDLING_MANGLER), fra, til
        )
        if (feiledeSaker.isEmpty()) return

        append("\n❌ *Feilede saker:*\n")
        feiledeSaker.take(10).forEach { sak ->
            append("• `${sak.rinasakId}` — `${sak.bucType}` — ${sak.status}\n")
        }
        if (feiledeSaker.size > 10) {
            append("_Totalt: `${fmt(feiledeSaker.size.toLong())}` feilede saker denne måneden_\n")
        }
    }

    private fun StringBuilder.appendNaaværendeStatus() {
        data class StatusLinje(val label: String, val count: Long)

        val linjer = listOf(
            StatusLinje("Nye saker (NY_SAK)", repository.countByStatus(NY_SAK)),
            StatusLinje("Uvirksomme", repository.countByStatus(UVIRKSOM)),
            StatusLinje("Til avslutning",
                repository.countByStatus(TIL_AVSLUTNING_GLOBALT) +
                        repository.countByStatus(TIL_AVSLUTNING_LOKALT)),
            StatusLinje("Til arkivering", repository.countByStatus(TIL_ARKIVERING)),
            StatusLinje("Feilet",
                repository.countByStatus(HANDLING_FEILET) +
                        repository.countByStatus(HANDLING_MANGLER)),
        ).filter { it.count > 0 }

        if (linjer.isEmpty()) return

        append("\n📋 *Nåværende status:*\n")
        linjer.forEach { (label, count) ->
            append("• $label: `${fmt(count)}`\n")
        }
    }

    private fun fmt(n: Long) = numberFormat.format(n)
}

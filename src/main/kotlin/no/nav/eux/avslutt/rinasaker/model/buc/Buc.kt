package no.nav.eux.avslutt.rinasaker.model.buc

import no.nav.eux.avslutt.rinasaker.model.buc.BucAvsluttScope.GLOBALT
import no.nav.eux.avslutt.rinasaker.model.buc.BucAvsluttScope.LOKALT
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak

data class Buc(
    val navn: String,
    val antallDagerBeforeUvirksom: Long,
    val antallDagerBeforeArkivering: Long = 180,
    val sisteSedForAvslutningAutomatisk: List<String> = emptyList(),
    val sedExistsForAvslutningAutomatisk: List<String> = emptyList(),
    val kreverSakseier: Boolean,
    val bucAvsluttScope: BucAvsluttScope,
    val opprettOppgave: Boolean,
)

enum class BucAvsluttScope(
    val tilAvslutningStatus: Rinasak.Status
) {
    LOKALT(Rinasak.Status.TIL_AVSLUTNING_LOKALT),
    GLOBALT(Rinasak.Status.TIL_AVSLUTNING_GLOBALT),
}

val bucList = listOf(
    Buc(
        navn = "FB_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("F002"),
        kreverSakseier = true,
        bucAvsluttScope = GLOBALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "FB_BUC_02",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("F017"),
        kreverSakseier = true,
        bucAvsluttScope = GLOBALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "FB_BUC_04",
        antallDagerBeforeUvirksom = 90,
        sedExistsForAvslutningAutomatisk = listOf("F003"),
        kreverSakseier = false,
        bucAvsluttScope = LOKALT,
        opprettOppgave = false,
    ),
    Buc(
        navn = "H_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("H002"),
        kreverSakseier = false,
        bucAvsluttScope = LOKALT,
        opprettOppgave = true,
    ),
)

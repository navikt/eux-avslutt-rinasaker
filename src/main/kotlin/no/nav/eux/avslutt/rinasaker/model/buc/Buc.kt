package no.nav.eux.avslutt.rinasaker.model.buc

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak

data class Buc(
    val navn: String,
    val antallDagerBeforeUvirksom: Long,
    val antallDagerBeforeArkivering: Long = 180,
    val sisteSedForAvslutningAutomatisk: List<String> = emptyList(),
    val sisteSedForAvslutningAutomatiskKrevesSendtFraNav: Boolean = false,
    val sedExistsForAvslutningAutomatisk: List<String> = emptyList(),
    val mottattSedExistsForAvslutningAutomatisk: List<String> = emptyList(),
    val sentSedExistsForAvslutningAutomatisk: List<String> = emptyList(),
    val bucAvsluttScopeSakseier: BucAvsluttScope? = null,
    val bucAvsluttScopeMotpart: BucAvsluttScope? = null,
    val opprettOppgave: Boolean,
)

enum class BucAvsluttScope(
    val tilAvslutningStatus: Rinasak.Status
) {
    AVSLUTT_LOKALT(Rinasak.Status.TIL_AVSLUTNING_LOKALT),
    AVSLUTT_GLOBALT(Rinasak.Status.TIL_AVSLUTNING_GLOBALT),
}

val bucList = listOf(
    Buc(
        navn = "FB_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("F002"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "FB_BUC_02",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("F017"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "FB_BUC_04",
        antallDagerBeforeUvirksom = 90,
        sedExistsForAvslutningAutomatisk = listOf("F003"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = false,
    ),
    Buc(
        navn = "H_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("H002"),
        sisteSedForAvslutningAutomatiskKrevesSendtFraNav = true,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
)

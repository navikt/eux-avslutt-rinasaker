package no.nav.eux.avslutt.rinasaker.model.buc

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.TIL_AVSLUTNING_GLOBALT
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.TIL_AVSLUTNING_LOKALT

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
    AVSLUTT_LOKALT(tilAvslutningStatus = TIL_AVSLUTNING_LOKALT),
    AVSLUTT_GLOBALT(tilAvslutningStatus = TIL_AVSLUTNING_GLOBALT),
}

val hBucList = listOf(
    Buc(
        navn = "H_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("H002"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
)

val fbBucList = listOf(
    Buc(
        navn = "FB_BUC_01",
        antallDagerBeforeUvirksom = 120,
        antallDagerBeforeArkivering = 400,
        sisteSedForAvslutningAutomatisk = listOf("F002"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        sisteSedForAvslutningAutomatiskKrevesSendtFraNav = true,
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
)

val ubBucList = listOf(
    Buc(
        navn = "UB_BUC_01",
        antallDagerBeforeUvirksom = 90,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("U002", "U004", "U017"),
        opprettOppgave = false,
    ),
    Buc(
        navn = "UB_BUC_02",
        antallDagerBeforeUvirksom = 180,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("U008", "U014", "H070"),
        sentSedExistsForAvslutningAutomatisk = listOf("U009", "H070"),
        opprettOppgave = false,
    ),
    Buc(
        navn = "UB_BUC_03",
        antallDagerBeforeUvirksom = 90,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("U019", "H070"),
        sentSedExistsForAvslutningAutomatisk = listOf("H070"),
        opprettOppgave = false,
    ),
    Buc(
        navn = "UB_BUC_04",
        antallDagerBeforeUvirksom = 90,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        sedExistsForAvslutningAutomatisk = listOf("U024"),
        opprettOppgave = false,
    ),
)

val sBucList = listOf(
    Buc(
        navn = "S_BUC_12",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S055"),
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_14",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S046"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_14a",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S047"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_14b",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S048"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_15",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S057"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_17",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S003"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_17a",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S005"),
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "S_BUC_24",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutningAutomatisk = listOf("S041"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
)

val pBucList = listOf(
    Buc(
        navn = "P_BUC_01",
        antallDagerBeforeUvirksom = 10000,
        sisteSedForAvslutningAutomatisk = listOf("disabled"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "P_BUC_03",
        antallDagerBeforeUvirksom = 10000,
        sisteSedForAvslutningAutomatisk = listOf("disabled"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "P_BUC_06",
        antallDagerBeforeUvirksom = 10000,
        sisteSedForAvslutningAutomatisk = listOf("disabled"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
    Buc(
        navn = "P_BUC_07",
        antallDagerBeforeUvirksom = 10000,
        sisteSedForAvslutningAutomatisk = listOf("disabled"),
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_LOKALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        opprettOppgave = true,
    ),
)

val bucList =
    fbBucList + hBucList + ubBucList + sBucList

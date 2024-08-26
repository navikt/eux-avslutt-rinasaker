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
        antallDagerBeforeUvirksom = 90,
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
        antallDagerBeforeUvirksom = 90,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("U014", "H070"),
        sentSedExistsForAvslutningAutomatisk = listOf("H070"),
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
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        sedExistsForAvslutningAutomatisk = listOf("U024"),
        opprettOppgave = false,
    ),
)

val sBucList = emptyList<Buc>()

val bucList =
    fbBucList + hBucList + ubBucList + sBucList

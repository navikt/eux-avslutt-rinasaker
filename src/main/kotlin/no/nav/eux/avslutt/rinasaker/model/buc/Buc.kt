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

    /**
     * 1. Vi mangler mulighet til å skille mellom motpart og sakseier slik det ønskes.
     * Er det mulig å finne felles regler?
     *
     * 2. Vi mangler telling etter start-sed, er det ok å telle dager for uvirksom på en annen måte?
     *
     * 3. Skal det lukkes globalt eller lokalt (for sakseier og motpart?)
     */
    Buc(
        navn = "P_BUC_01",
        antallDagerBeforeUvirksom = 270,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P6000", "P7000"),
        sentSedExistsForAvslutningAutomatisk = listOf("P6000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Vi mangler mulighet til å skille mellom motpart og sakseier slik det ønskes.
     * Er det mulig å finne felles regler?
     *
     * 2. Vi mangler telling etter start-sed, er det ok å telle dager for uvirksom på en annen måte?
     *
     * 3. Skal det lukkes globalt eller lokalt (for sakseier og motpart?)
     */
    Buc(
        navn = "P_BUC_02",
        antallDagerBeforeUvirksom = 270,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P6000", "P7000"),
        sentSedExistsForAvslutningAutomatisk = listOf("P6000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Vi mangler mulighet til å skille mellom motpart og sakseier slik det ønskes.
     * Er det mulig å finne felles regler?
     *
     * 2. Vi mangler telling etter start-sed, er det ok å telle dager for uvirksom på en annen måte?
     *
     * 3. Skal det lukkes globalt eller lokalt (for sakseier og motpart?)
     */
    Buc(
        navn = "P_BUC_03",
        antallDagerBeforeUvirksom = 270,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P6000", "P7000"),
        sentSedExistsForAvslutningAutomatisk = listOf("P6000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Kan vi telle fra siste aktivitet? eller må det telles fra P8000?
     * 2. Ok at 12 måneder defineres som 365 dager?
     */
    Buc(
        navn = "P_BUC_05",
        antallDagerBeforeUvirksom = 365,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P8000"),
        sentSedExistsForAvslutningAutomatisk = listOf("P8000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Vi har ingen mekansime for å telle fra start-sed, kan vi telle fra siste SED?
     */
    Buc(
        navn = "P_BUC_06",
        antallDagerBeforeUvirksom = 28,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Lokalt eller globalt avslutt scope?
     * 2. Kan det sjekkes på aktivitet og sedExists, ikke direkte 7 dager etter P12000?
     */
    Buc(
        navn = "P_BUC_07",
        antallDagerBeforeUvirksom = 7,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P12000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Lokalt eller globalt avslutt scope?
     * 2. Kan det sjekkes på aktivitet og sedExists, ikke direkte 7 dager etter P14000?
     */
    Buc(
        navn = "P_BUC_09",
        antallDagerBeforeUvirksom = 7,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P14000"),
        sentSedExistsForAvslutningAutomatisk = listOf("P14000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    /**
     * 1. Lokalt eller globalt avslutt scope?
     * 2. Kan det sjekkes på aktivitet og sedExists, ikke direkte 7 dager etter P14000?
     */
    Buc(
        navn = "P_BUC_10",
        antallDagerBeforeUvirksom = 7,
        bucAvsluttScopeSakseier = BucAvsluttScope.AVSLUTT_GLOBALT,
        bucAvsluttScopeMotpart = BucAvsluttScope.AVSLUTT_LOKALT,
        mottattSedExistsForAvslutningAutomatisk = listOf("P7000"),
        sentSedExistsForAvslutningAutomatisk = listOf("P7000"),
        antallDagerBeforeArkivering = 180,
        opprettOppgave = false,
    ),

    // Arkivering: Er det ok å definere 6 måneder som 180 dager?
)

val bucList =
    fbBucList + hBucList + ubBucList + sBucList

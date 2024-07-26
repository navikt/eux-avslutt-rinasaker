package no.nav.eux.avslutt.rinasaker.model.buc

import no.nav.eux.avslutt.rinasaker.model.buc.BucAvsluttScope.GLOBALT
import no.nav.eux.avslutt.rinasaker.model.buc.BucAvsluttScope.LOKALT
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak

data class Buc(
    val navn: String,
    val antallDagerBeforeUvirksom: Long,
    val antallDagerBeforeArkivering: Long = -1,
    val sisteSedForAvslutningAutomatisk: String,
    val kreverSakseier: Boolean,
    val bucAvsluttScope: BucAvsluttScope,
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
        antallDagerBeforeUvirksom = -1,
        sisteSedForAvslutningAutomatisk = "F002",
        kreverSakseier = true,
        bucAvsluttScope = GLOBALT,
    ),
    Buc(
        navn = "FB_BUC_02",
        antallDagerBeforeUvirksom = -1,
        sisteSedForAvslutningAutomatisk = "F017",
        kreverSakseier = true,
        bucAvsluttScope = GLOBALT,
    ),
    Buc(
        navn = "FB_BUC_04",
        antallDagerBeforeUvirksom = -1,
        sisteSedForAvslutningAutomatisk = "F003",
        kreverSakseier = false,
        bucAvsluttScope = LOKALT,
    ),
    Buc(
        navn = "H_BUC_01",
        antallDagerBeforeUvirksom = -1,
        sisteSedForAvslutningAutomatisk = "H002",
        kreverSakseier = false,
        bucAvsluttScope = LOKALT,
    ),
)

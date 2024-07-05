package no.nav.eux.avslutt.rinasaker.model.buc

data class Buc(
    val navn: String,
    val antallDagerBeforeUvirksom: Int,
    val sisteSedForAvslutning: String,
)

val bucList = listOf(
    Buc(
        navn = "FB_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutning = "F002"
    ),
    Buc(
        navn = "FB_BUC_02",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutning = "F017"
    ),
)

val bucMap = bucList.associateBy { it.navn }

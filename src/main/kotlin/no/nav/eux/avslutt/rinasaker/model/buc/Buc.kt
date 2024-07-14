package no.nav.eux.avslutt.rinasaker.model.buc

data class Buc(
    val navn: String,
    val antallDagerBeforeUvirksom: Long,
    val sisteSedForAvslutning: String,
    val kreverSakseier: Boolean,
)

val bucList = listOf(
    Buc(
        navn = "FB_BUC_01",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutning = "F002",
        kreverSakseier = true
    ),
    Buc(
        navn = "FB_BUC_02",
        antallDagerBeforeUvirksom = 90,
        sisteSedForAvslutning = "F017",
        kreverSakseier = true
    ),
)

val bucMap = bucList.associateBy { it.navn }

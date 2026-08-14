package no.nav.eux.avslutt.rinasaker.webapp

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.ALLEREDE_AVSLUTTET
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.TIL_AVSLUTNING_GLOBALT
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.TIL_AVSLUTNING_LOKALT
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
import no.nav.eux.avslutt.rinasaker.webapp.common.AbstractTest
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AlleredeAvsluttetTest : AbstractTest() {

    @Test
    fun `saker allerede avsluttet i RINA settes til ALLEREDE_AVSLUTTET`() {
        lagreRinasak(101, UVIRKSOM, "FB_BUC_01")
        lagreRinasak(102, TIL_AVSLUTNING_GLOBALT, "FB_BUC_01")
        lagreRinasak(103, TIL_AVSLUTNING_LOKALT, "FB_BUC_04")

        execute(prosess = "til-avslutning")
        101 er ALLEREDE_AVSLUTTET

        execute(prosess = "avslutt")
        102 er ALLEREDE_AVSLUTTET
        103 er ALLEREDE_AVSLUTTET

        assertThat(requestBodies["/api/v1/rinasaker/102/avsluttGlobalt"]).isNull()
        assertThat(requestBodies["/api/v1/rinasaker/103/avsluttLokalt"]).isNull()
    }

    fun lagreRinasak(rinasakId: Int, status: Rinasak.Status, bucType: String) {
        rinasakRepository.save(
            Rinasak(
                rinasakStatusUuid = rinasakId.uuid,
                rinasakId = rinasakId,
                status = status,
                bucType = bucType,
                erSakseier = true,
            )
        )
    }
}

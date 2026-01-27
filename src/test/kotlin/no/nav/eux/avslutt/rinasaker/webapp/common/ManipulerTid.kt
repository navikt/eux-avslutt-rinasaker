package no.nav.eux.avslutt.rinasaker.webapp.common

import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import java.time.LocalDateTime.now

infix fun DokumentRepository.manipulerEndretTidspunktForSed(sedId: Int): Dokument =
    findBySedIdAndSedVersjon(sedId = sedId.uuid, 1)!!
        .copy(endretTidspunkt = now().minusDays(dagerUvirksom))
        .let { save(it) }

infix fun RinasakRepository.manipulerEndretTidspunktForCaseIdArkivering(caseId: Int): Rinasak =
    findByRinasakId(caseId)!!
        .copy(endretTidspunkt = now().minusDays(dagerArkivering))
        .let { save(it) }

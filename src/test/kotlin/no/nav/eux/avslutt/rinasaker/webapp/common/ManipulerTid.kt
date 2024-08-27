package no.nav.eux.avslutt.rinasaker.webapp.common

import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import java.time.LocalDateTime.now

infix fun DokumentRepository.manipulerEndretTidspunktForSed(sedId: Int) =
    findBySedIdAndSedVersjon(sedId = sedId.uuid, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerUvirksom))
        .let { save(it) }

infix fun RinasakRepository.manipulerEndretTidspunktForCaseIdArkivering(caseId: Int) =
    findByRinasakId(caseId)!!
        .copy(endretTidspunkt = now().minusDays(dagerArkivering))
        .let { save(it) }

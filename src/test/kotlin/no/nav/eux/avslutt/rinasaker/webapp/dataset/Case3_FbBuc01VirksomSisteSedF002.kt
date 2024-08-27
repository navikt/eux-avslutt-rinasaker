package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.avslutt.rinasaker.webapp.common.*
import java.time.LocalDateTime.now

val fbBuc01VirksomSisteSedF002_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 3,
            processDefinitionName = "FB_BUC_01",
            whoami = whoamiNav,
            creator = caseCreatorOrgNav
        )
    )
)

val fbBuc01VirksomSisteSedF002_sed1 = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 3.documentId,
            type = "F002",
            caseId = 3,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = offsetDateTime
        )
    )
)

val fbBuc01VirksomSisteSedF002_sed2 = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 4.documentId,
            type = "F002",
            caseId = 3,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = offsetDateTime
        )
    )
)

fun DokumentRepository.case3_manipulerOpprettetTidspunkt() {
    val dokumentEldre = findBySedIdAndSedVersjon(uuid3, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerUvirksom))
    save(dokumentEldre)
    val dokumentNyere = findBySedIdAndSedVersjon(uuid4, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerVirksom))
    save(dokumentNyere)
}

fun RinasakRepository.case3_manipulerEndretTidspunktArkivering() {
    val dokumentEldre = findByRinasakId(3)!!
        .copy(endretTidspunkt = now().minusDays(dagerArkivering))
    save(dokumentEldre)
    val dokumentNyere = findByRinasakId(3)!!
        .copy(endretTidspunkt = now().minusDays(dagerArkivering))
    save(dokumentNyere)
}


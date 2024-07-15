package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.*
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.webapp.common.dagerUvirksom
import no.nav.eux.avslutt.rinasaker.webapp.common.dagerVirksom
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid3
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid4
import java.time.LocalDateTime.now
import java.time.OffsetDateTime

val fbBuc01VirksomSisteSedF002_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 3,
            processDefinitionName = "F_BUC_01",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = "NO:NAVAT06"))
        )
    )
)

val fbBuc01VirksomSisteSedF002_sed1 = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "F_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000003",
            type = "F002",
            caseId = 3,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
        )
    )
)

val fbBuc01VirksomSisteSedF002_sed2 = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "F_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000004",
            type = "F002",
            caseId = 3,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
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

package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.*
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.webapp.common.dagerUvirksom
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid1
import java.time.LocalDateTime.now
import java.time.OffsetDateTime

val fbBuc01UvirksomSisteSedF002_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 1,
            processDefinitionName = "F_BUC_01",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(id = "NO:NAVAT06")
        )
    )
)

val fbBuc01UvirksomSisteSedF002_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "F_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000001",
            type = "F002",
            caseId = 1,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
        )
    )
)

fun DokumentRepository.case1_manipulerOpprettetTidspunkt() {
    val dokument = findBySedIdAndSedVersjon(uuid1, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerUvirksom))
    save(dokument)
}

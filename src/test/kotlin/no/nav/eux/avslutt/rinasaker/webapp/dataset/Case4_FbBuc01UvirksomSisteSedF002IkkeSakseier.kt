package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.*
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.webapp.common.dagerUvirksom
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid5
import java.time.LocalDateTime.now
import java.time.OffsetDateTime

val fbBuc01UvirksomSisteSedF002IkkeSakseier_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 4,
            processDefinitionName = "FB_BUC_01",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = "NO:not-me"))
        )
    )
)

val fbBuc01UvirksomSisteSedF002IkkeSakseier_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000005",
            type = "F002",
            caseId = 4,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
        )
    )
)

fun DokumentRepository.case4_manipulerOpprettetTidspunkt() {
    val dokument = findBySedIdAndSedVersjon(uuid5, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerUvirksom))
    save(dokument)
}

package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.*
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import java.time.OffsetDateTime

val fbBuc04UvirksomSedF003_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 5,
            processDefinitionName = "FB_BUC_04",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = "NO:NAVAT06"))
        )
    )
)

val fbBuc04UvirksomSedF003_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_04",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000006",
            type = "F003",
            caseId = 5,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
        )
    )
)

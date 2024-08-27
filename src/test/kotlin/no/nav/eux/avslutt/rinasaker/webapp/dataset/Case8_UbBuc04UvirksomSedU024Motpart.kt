package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.*
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.webapp.common.documentId
import no.nav.eux.avslutt.rinasaker.webapp.common.offsetDateTime1

private const val caseId = 8

val ubBuc04UvirksomSedU024Motpart_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = caseId,
            processDefinitionName = "UB_BUC_04",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = "NO:not-me"))
        )
    )
)

val ubBuc04UvirksomSedU024Motpart_sed = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "UB_BUC_04",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 9.documentId,
            type = "U024",
            caseId = caseId,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = offsetDateTime1
        )
    )
)

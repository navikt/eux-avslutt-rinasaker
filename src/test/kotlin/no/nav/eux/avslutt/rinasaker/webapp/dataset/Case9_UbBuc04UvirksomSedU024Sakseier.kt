package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.webapp.common.*

private const val caseId = 9

val ubBuc04UvirksomSedU024Sakseier_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = caseId,
            processDefinitionName = "UB_BUC_04",
            whoami = whoamiNav,
            creator = caseCreatorOrgNav
        )
    )
)

val ubBuc04UvirksomSedU024Sakseier_sed = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "UB_BUC_04",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 10.documentId,
            type = "U024",
            caseId = caseId,
            versions = documentVersions,
            creationDate = offsetDateTime
        )
    )
)

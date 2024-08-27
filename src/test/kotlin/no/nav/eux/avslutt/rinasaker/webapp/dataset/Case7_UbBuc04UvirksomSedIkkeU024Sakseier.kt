package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.webapp.common.caseCreatorOrgNav
import no.nav.eux.avslutt.rinasaker.webapp.common.documentId
import no.nav.eux.avslutt.rinasaker.webapp.common.offsetDateTime1
import no.nav.eux.avslutt.rinasaker.webapp.common.whoamiNav

private const val caseId = 7

val ubBuc04UvirksomSedIkkeU024_case = KafkaRinaCase(
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

val ubBuc04UvirksomSedIkkeU024_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "UB_BUC_04",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 8.documentId,
            type = "H001",
            caseId = caseId,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = offsetDateTime1
        )
    )
)

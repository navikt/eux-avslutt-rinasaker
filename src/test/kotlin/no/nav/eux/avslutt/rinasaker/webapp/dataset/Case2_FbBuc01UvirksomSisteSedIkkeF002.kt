package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.webapp.common.*

val fbBuc01UvirksomSisteSedIkkeF002_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 2,
            processDefinitionName = "FB_BUC_01",
            whoami = whoamiNav,
            creator = caseCreatorOrgNav
        )
    )
)

val fbBuc01UvirksomSisteSedIkkeF002_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 2.documentId,
            type = "F001",
            caseId = 2,
            versions = documentVersions,
            creationDate = offsetDateTime
        )
    )
)

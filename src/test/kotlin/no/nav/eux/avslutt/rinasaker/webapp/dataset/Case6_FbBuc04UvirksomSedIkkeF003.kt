package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.webapp.common.*

val fbBuc04UvirksomSedIkkeF003_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 6,
            processDefinitionName = "FB_BUC_04",
            whoami = whoamiNav,
            creator = caseCreatorOrgNav
        )
    )
)

val fbBuc04UvirksomSedIkkeF003_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_04",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 7.documentId,
            type = "H001",
            caseId = 6,
            versions = documentVersions,
            creationDate = offsetDateTime
        )
    )
)

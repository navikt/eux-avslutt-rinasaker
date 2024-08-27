package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.webapp.common.caseCreatorOrgNotNav
import no.nav.eux.avslutt.rinasaker.webapp.common.documentId
import no.nav.eux.avslutt.rinasaker.webapp.common.offsetDateTime
import no.nav.eux.avslutt.rinasaker.webapp.common.whoamiNav

val fbBuc01UvirksomSisteSedF002IkkeSakseier_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 4,
            processDefinitionName = "FB_BUC_01",
            whoami = whoamiNav,
            creator = caseCreatorOrgNotNav
        )
    )
)

val fbBuc01UvirksomSisteSedF002IkkeSakseier_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = 5.documentId,
            type = "F002",
            caseId = 4,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = offsetDateTime
        )
    )
)

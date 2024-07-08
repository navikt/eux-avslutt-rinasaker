package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCasePayload
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import java.time.LocalDateTime


fun kafkaRinaCase(rinasakId: Int) = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(KafkaRinaCaseRestCase(rinasakId, "H_BUC_01"))
)

fun kafkaRinaDocument(
    sedId: String = "eb172665832f402794a6946e7efcd0f2",
    sedVersjon: Int = 1,
    rinasakId: Int = 1
) = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = sedId,
            type = "H001",
            caseId = rinasakId,
            versions = listOf(
                KafkaRinaDocumentVersions(id = sedVersjon)
            ),
            creationDate = LocalDateTime.parse("2021-06-01T12:00:00")
        )
    )
)

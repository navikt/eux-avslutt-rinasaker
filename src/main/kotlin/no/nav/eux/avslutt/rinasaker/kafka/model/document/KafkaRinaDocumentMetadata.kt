package no.nav.eux.avslutt.rinasaker.kafka.model.document

import java.time.OffsetDateTime

data class KafkaRinaDocumentMetadata(
    val id: String,
    val type: String,
    val caseId: Int,
    val versions: List<KafkaRinaDocumentVersions>,
    val creationDate: OffsetDateTime,
)

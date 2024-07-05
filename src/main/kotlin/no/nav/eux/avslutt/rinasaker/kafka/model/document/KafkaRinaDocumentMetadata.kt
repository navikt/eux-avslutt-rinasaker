package no.nav.eux.avslutt.rinasaker.kafka.model.document

import java.time.LocalDateTime

data class KafkaRinaDocumentMetadata(
    val type: String,
    val caseId: Int,
    val creationDate: LocalDateTime,
)

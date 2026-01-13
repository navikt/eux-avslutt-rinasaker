package no.nav.eux.avslutt.rinasaker.kafka.model.document

import com.fasterxml.jackson.annotation.JsonProperty

data class KafkaRinaDocumentPayload(
    @param:JsonProperty("DOCUMENT_METADATA")
    val documentMetadata: KafkaRinaDocumentMetadata
)

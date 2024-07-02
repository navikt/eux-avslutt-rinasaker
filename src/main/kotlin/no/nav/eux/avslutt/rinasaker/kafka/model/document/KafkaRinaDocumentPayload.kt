package no.nav.eux.avslutt.rinasaker.kafka.model.document

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata

data class KafkaRinaDocumentPayload(
    @JsonProperty("DOCUMENT_METADATA")
    val documentMetadata: KafkaRinaDocumentMetadata
)

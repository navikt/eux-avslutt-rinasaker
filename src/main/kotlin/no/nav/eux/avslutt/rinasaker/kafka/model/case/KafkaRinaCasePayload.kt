package no.nav.eux.avslutt.rinasaker.kafka.model.case

import com.fasterxml.jackson.annotation.JsonProperty

data class KafkaRinaCasePayload(
    @JsonProperty("REST_CASE")
    val restCase: KafkaRinaCaseRestCase
)

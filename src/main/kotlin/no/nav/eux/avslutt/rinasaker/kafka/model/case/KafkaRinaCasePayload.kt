package no.nav.eux.avslutt.rinasaker.kafka.model.case

import com.fasterxml.jackson.annotation.JsonProperty

data class KafkaRinaCasePayload(
    @param:JsonProperty("REST_CASE")
    val restCase: KafkaRinaCaseRestCase
)

package no.nav.eux.avslutt.rinasaker.kafka.model.case

data class KafkaRinaCase(
    val caseEventType: String,
    val payLoad: KafkaRinaCasePayload
)

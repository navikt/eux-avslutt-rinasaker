package no.nav.eux.avslutt.rinasaker.kafka.model.case

data class KafkaRinaCaseRestCase(
    val id: Int,
    val processDefinitionName: String,
    val whoami: KafkaRinaCaseRestCaseWhoami,
    val creator: KafkaRinaCaseRestCaseCreator
)

data class KafkaRinaCaseRestCaseWhoami(
    val id: String
)

data class KafkaRinaCaseRestCaseCreator(
    val id: String
)

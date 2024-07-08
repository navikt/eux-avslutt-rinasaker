package no.nav.eux.avslutt.rinasaker.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.service.PopulerService
import no.nav.eux.avslutt.rinasaker.service.mdc
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.fromString

@Service
class EuxRinaCaseEventsKafkaListener(
    val populerService: PopulerService
) {

    val log = logger {}

    @KafkaListener(
        id = "eux-avslutt-rinasaker-case",
        topics = ["\${kafka.topics.eux-rina-case-events-v1}"],
        containerFactory = "rinaCaseKafkaListenerContainerFactory"
    )
    fun case(consumerRecord: ConsumerRecord<String, KafkaRinaCase>) {
        val caseId = consumerRecord.value().payLoad.restCase.id
        val processDefinitionName = consumerRecord.value().payLoad.restCase.processDefinitionName
        mdc(rinasakId = caseId, bucType = processDefinitionName)
        log.info { "Mottok rina case event" }
        populerService.leggTilRinasak(
            rinasakId = caseId,
            bucType = processDefinitionName
        )
    }

    @KafkaListener(
        id = "eux-avslutt-rinasaker-document",
        topics = ["\${kafka.topics.eux-rina-document-events-v1}"],
        containerFactory = "rinaDocumentKafkaListenerContainerFactory"
    )
    fun document(consumerRecord: ConsumerRecord<String, KafkaRinaDocument>) {
        val documentEventType = consumerRecord.value().documentEventType
        val caseId = consumerRecord.value().payLoad.documentMetadata.caseId
        val bucType = consumerRecord.value().buc
        mdc(rinasakId = caseId, bucType = bucType)
        log.info { "Mottok rina document event" }
        when (documentEventType) {
            "SENT_DOCUMENT" -> log.info { "Mottok documentEventType: $documentEventType" }
            else -> log.info { "Mottok documentEventType, ignorerer: $documentEventType" }
        }
        populerService.leggTilDokument(
            rinasakId = caseId,
            sedId = uuid(consumerRecord.value().payLoad.documentMetadata.id),
            sedVersjon = consumerRecord.value().payLoad.documentMetadata.versions.first().id,
            sedType = consumerRecord.value().payLoad.documentMetadata.type
        )
    }
}

fun uuid(uuidWithoutDash: String): UUID =
    fromString(uuidString(uuidWithoutDash = uuidWithoutDash))

fun uuidString(uuidWithoutDash: String): String = with(uuidWithoutDash) {
    listOf(substring(0, 8), substring(8, 12), substring(12, 16), substring(16, 20), substring(20, 32))
        .joinToString(separator = "-")
}

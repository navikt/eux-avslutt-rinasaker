package no.nav.eux.avslutt.rinasaker.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCase
import no.nav.eux.avslutt.rinasaker.service.mdc
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EuxRinaCaseEventsKafkaListener {

    val log = logger {}

    @KafkaListener(
        id = "eux-avslutt-rinasaker-case-draft-5",
        topics = ["\${kafka.topics.eux-rina-case-events-v1}"],
        containerFactory = "rinaCaseKafkaListenerContainerFactory"
    )
    fun case(consumerRecord: ConsumerRecord<String, KafkaRinaCase>) {
        val caseId = consumerRecord.value().payLoad.restCase.id
        val processDefinitionName = consumerRecord.value().payLoad.restCase.processDefinitionName
        mdc(rinasakId = caseId, bucType = processDefinitionName)
        log.info { "Mottok rina case event" }
    }

    @KafkaListener(
        id = "eux-avslutt-rinasaker-document-draft-5",
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
            "SENT_DOCUMENT" -> log.info { "Mottok documentEventType: $documentEventType"}
            else -> log.info { "Mottok documentEventType, ignorerer: $documentEventType" }
        }
    }
}

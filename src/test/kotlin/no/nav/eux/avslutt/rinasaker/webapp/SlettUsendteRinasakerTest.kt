package no.nav.eux.avslutt.rinasaker.webapp

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.NY_SAK
import no.nav.eux.avslutt.rinasaker.webapp.dataset.kafkaRinaCase
import no.nav.eux.avslutt.rinasaker.webapp.dataset.kafkaRinaDocument
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Test

class SlettUsendteRinasakerTest : AbstractTest() {

    @Test
    fun `Nye rinasaker og dokumenter fra Kafka - sletting staged og eksekvert`() {
        assertThat(kafka.isRunning).isTrue
        assertThat(postgres.isRunning).isTrue
        kafkaTemplate.send("eessibasis.eux-rina-case-events-v1", kafkaRinaCase(1))
        kafkaTemplate.send("eessibasis.eux-rina-case-events-v1", kafkaRinaCase(2))
        kafkaTemplate.send("eessibasis.eux-rina-case-events-v1", kafkaRinaCase(3))
        kafkaTemplate.send("eessibasis.eux-rina-case-events-v1", kafkaRinaCase(4))
        kafkaTemplate.send("eessibasis.eux-rina-case-events-v1", kafkaRinaCase(5))
        await untilCallTo {
            rinasakRepository.findAllByStatus(NY_SAK)
        } has {
            size == 5
        }
        kafkaTemplate.send("eessibasis.eux-rina-document-events-v1", kafkaRinaDocument(rinasakId = 1, sedVersjon = 1))
        kafkaTemplate.send("eessibasis.eux-rina-document-events-v1", kafkaRinaDocument(rinasakId = 2, sedVersjon = 2))
        await untilCallTo {
            dokumentRepository.findByRinasakId(1)
        } has {
            size == 1
        }
        await untilCallTo {
            dokumentRepository.findByRinasakId(2)
        } has {
            size == 1
        }
    }

}

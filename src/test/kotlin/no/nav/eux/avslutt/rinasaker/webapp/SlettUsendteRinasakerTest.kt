package no.nav.eux.avslutt.rinasaker.webapp

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.NY_SAK
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.UVIRKSOM
import no.nav.eux.avslutt.rinasaker.webapp.common.kafkaTopicRinaCaseEvents
import no.nav.eux.avslutt.rinasaker.webapp.common.kafkaTopicRinaDocumentEvents
import no.nav.eux.avslutt.rinasaker.webapp.dataset.*
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Test

class SlettUsendteRinasakerTest : AbstractTest() {

    @Test
    fun `Nye rinasaker og dokumenter fra Kafka - avslutning staged og eksekvert`() {
        isRunning()
        lagSaker()
        verifiserSakerOpprettet()
        lagDokumenter()
        verifiserDokumenterOpprettet()
        manipulerOpprettetTidspunkt()
        execute(prosess = "sett-uvirksom")
        verifiserVirksom()
    }

    fun isRunning() {
        assertThat(kafka.isRunning).isTrue
        assertThat(postgres.isRunning).isTrue
    }

    fun lagSaker() {
        kafkaTopicRinaCaseEvents send fbBuc01UvirksomSisteSedF002_case
        kafkaTopicRinaCaseEvents send fbBuc01UvirksomSisteSedIkkeF002_case
        kafkaTopicRinaCaseEvents send fbBuc01VirksomSisteSedF002_case
    }

    fun verifiserSakerOpprettet() {
        await untilCallTo {
            rinasakRepository.findAll()
        } has {
            size == 3
        }
    }

    fun lagDokumenter() {
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedF002_sed
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedIkkeF002_sed
        kafkaTopicRinaDocumentEvents send fbBuc01VirksomSisteSedF002_sed1
        kafkaTopicRinaDocumentEvents send fbBuc01VirksomSisteSedF002_sed2
    }

    fun verifiserDokumenterOpprettet() {
        await untilCallTo {
            dokumentRepository.findAll()
        } has {
            size == 4
        }
    }

    fun manipulerOpprettetTidspunkt() {
        dokumentRepository.case1_manipulerOpprettetTidspunkt()
        dokumentRepository.case2_manipulerOpprettetTidspunkt()
        dokumentRepository.case3_manipulerOpprettetTidspunkt()
    }

    fun verifiserVirksom() {
        1 er UVIRKSOM
        2 er UVIRKSOM
        3 er NY_SAK
    }

}

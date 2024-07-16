package no.nav.eux.avslutt.rinasaker.webapp

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.webapp.common.AbstractTest
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
        verifiserVirksomStatus()
        execute(prosess = "til-avslutning")
        verifiserTilAvslutningStatus()
        execute(prosess = "avslutt")
        verifiserAvsluttStatus()
        verifiserAvsluttKall()
        skrivUtEksterneKall()
    }

    fun isRunning() {
        assertThat(kafka.isRunning).isTrue
        assertThat(postgres.isRunning).isTrue
    }

    fun lagSaker() {
        kafkaTopicRinaCaseEvents send fbBuc01UvirksomSisteSedF002_case
        kafkaTopicRinaCaseEvents send fbBuc01UvirksomSisteSedIkkeF002_case
        kafkaTopicRinaCaseEvents send fbBuc01VirksomSisteSedF002_case
        kafkaTopicRinaCaseEvents send fbBuc01UvirksomSisteSedF002IkkeSakseier_case
        kafkaTopicRinaCaseEvents send fbBuc04UvirksomSisteSedF003_case
    }

    fun verifiserSakerOpprettet() {
        await untilCallTo {
            rinasakRepository.findAll()
        } has {
            size == 5
        }
    }

    fun lagDokumenter() {
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedF002_sed
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedIkkeF002_sed
        kafkaTopicRinaDocumentEvents send fbBuc01VirksomSisteSedF002_sed1
        kafkaTopicRinaDocumentEvents send fbBuc01VirksomSisteSedF002_sed2
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedF002IkkeSakseier_sed
        kafkaTopicRinaDocumentEvents send fbBuc04UvirksomSisteSedF003_sed
    }

    fun verifiserDokumenterOpprettet() {
        await untilCallTo {
            dokumentRepository.findAll()
        } has {
            size == 6
        }
    }

    fun manipulerOpprettetTidspunkt() {
        dokumentRepository.case1_manipulerOpprettetTidspunkt()
        dokumentRepository.case2_manipulerOpprettetTidspunkt()
        dokumentRepository.case3_manipulerOpprettetTidspunkt()
        dokumentRepository.case4_manipulerOpprettetTidspunkt()
        dokumentRepository.case5_manipulerOpprettetTidspunkt()
    }

    fun verifiserVirksomStatus() {
        1 er UVIRKSOM
        2 er UVIRKSOM
        3 er NY_SAK
        4 er UVIRKSOM
        5 er UVIRKSOM
    }

    fun verifiserTilAvslutningStatus() {
        1 er TIL_AVSLUTNING_GLOBALT
        2 er OPPRETT_OPPGAVE
        3 er NY_SAK
        4 er AVSLUTTES_AV_MOTPART
        5 er TIL_AVSLUTNING_LOKALT
    }

    fun verifiserAvsluttStatus() {
        1 er AVSLUTTET_GLOBALT
        2 er OPPRETT_OPPGAVE
        3 er NY_SAK
        4 er AVSLUTTES_AV_MOTPART
        5 er AVSLUTTET_LOKALT
    }

    fun verifiserAvsluttKall() {
        verifiserEksekvert("/api/v1/rinasaker/1/avsluttGlobalt")
        verifiserEksekvert("/api/v1/rinasaker/5/avsluttLokalt")
    }

}

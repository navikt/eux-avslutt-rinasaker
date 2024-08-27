package no.nav.eux.avslutt.rinasaker.webapp

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.*
import no.nav.eux.avslutt.rinasaker.webapp.common.*
import no.nav.eux.avslutt.rinasaker.webapp.dataset.*
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Test

class AvsluttRinasakerTest : AbstractTest() {

    @Test
    fun `Nye rinasaker og dokumenter fra Kafka - avslutning staged og eksekvert`() {
        isRunning()
        lagSaker()
        verifiserSakerOpprettet()
        lagDokumenter()
        verifiserDokumenterOpprettet()
        manipulerEndretTidspunkt()
        execute(prosess = "sett-uvirksom")
        verifiserVirksomStatus()
        execute(prosess = "til-avslutning")
        verifiserTilAvslutningStatus()
        execute(prosess = "avslutt")
        verifiserAvsluttStatus()
        verifiserAvsluttKall()
        skrivUtEksterneKall()
        execute(prosess = "til-arkivering")
        execute(prosess = "arkiver")
        verifiserAvsluttStatus()
        manipulerEndretTidspunktArkivering()
        execute(prosess = "til-arkivering")
        verifiserTilArkiveringStatus()
        execute(prosess = "arkiver")
        verifiserArkivertStatus()
        verifiserArkivertKall()
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
        kafkaTopicRinaCaseEvents send fbBuc04UvirksomSedF003_case
        kafkaTopicRinaCaseEvents send fbBuc04UvirksomSedIkkeF003_case
        kafkaTopicRinaCaseEvents send ubBuc04UvirksomSedIkkeU024_case
        kafkaTopicRinaCaseEvents send ubBuc04UvirksomSedU024Sakseier_case
        kafkaTopicRinaCaseEvents send ubBuc04UvirksomSedU024Motpart_case
    }

    fun verifiserSakerOpprettet() {
        await untilCallTo {
            rinasakRepository.findAll()
        } has {
            size == 9
        }
    }

    fun lagDokumenter() {
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedF002_sed
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedIkkeF002_sed
        kafkaTopicRinaDocumentEvents send fbBuc01VirksomSisteSedF002_sed1
        kafkaTopicRinaDocumentEvents send fbBuc01VirksomSisteSedF002_sed2
        kafkaTopicRinaDocumentEvents send fbBuc01UvirksomSisteSedF002IkkeSakseier_sed
        kafkaTopicRinaDocumentEvents send fbBuc04UvirksomSedF003_sed
        kafkaTopicRinaDocumentEvents send fbBuc04UvirksomSedIkkeF003_sed
        kafkaTopicRinaDocumentEvents send ubBuc04UvirksomSedIkkeU024_sed
        kafkaTopicRinaDocumentEvents send ubBuc04UvirksomSedU024Sakseier_sed
        kafkaTopicRinaDocumentEvents send ubBuc04UvirksomSedU024Motpart_sed
    }

    fun verifiserDokumenterOpprettet() {
        await untilCallTo {
            dokumentRepository.findAll()
        } has {
            size == 10
        }
    }

    fun manipulerEndretTidspunkt() {
        for (i in (1..2) + (5..10))
            dokumentRepository manipulerEndretTidspunktForSed i
        dokumentRepository.case3_manipulerOpprettetTidspunkt()
    }

    fun verifiserVirksomStatus() {
        1 er UVIRKSOM
        2 er UVIRKSOM
        3 er NY_SAK
        4 er UVIRKSOM
        5 er UVIRKSOM
        6 er UVIRKSOM
        7 er UVIRKSOM
        8 er UVIRKSOM
        9 er UVIRKSOM
    }

    fun verifiserTilAvslutningStatus() {
        1 er TIL_AVSLUTNING_GLOBALT
        2 er OPPRETT_OPPGAVE
        3 er NY_SAK
        4 er AVSLUTTES_AV_MOTPART
        5 er TIL_AVSLUTNING_LOKALT
        6 er UVIRKSOM
        7 er UVIRKSOM
        8 er TIL_AVSLUTNING_LOKALT
        9 er TIL_AVSLUTNING_GLOBALT
    }

    fun verifiserAvsluttStatus() {
        1 er AVSLUTTET_GLOBALT
        2 er OPPRETT_OPPGAVE
        3 er NY_SAK
        4 er AVSLUTTES_AV_MOTPART
        5 er AVSLUTTET_LOKALT
        6 er UVIRKSOM
        7 er UVIRKSOM
        8 er AVSLUTTET_LOKALT
        9 er AVSLUTTET_GLOBALT
    }

    fun verifiserAvsluttKall() {
        verifiserEksekvert("/api/v1/rinasaker/1/avsluttGlobalt")
        verifiserEksekvert("/api/v1/rinasaker/5/avsluttLokalt")
        verifiserEksekvert("/api/v1/rinasaker/8/avsluttLokalt")
        verifiserEksekvert("/api/v1/rinasaker/9/avsluttGlobalt")
    }

    fun manipulerEndretTidspunktArkivering() {
        for (i in (1..2) + (4..9))
            rinasakRepository manipulerEndretTidspunktForCaseIdArkivering i
        rinasakRepository.case3_manipulerEndretTidspunktArkivering()
    }

    fun verifiserTilArkiveringStatus() {
        1 er TIL_ARKIVERING
        2 er OPPRETT_OPPGAVE
        3 er NY_SAK
        4 er AVSLUTTES_AV_MOTPART
        5 er TIL_ARKIVERING
        6 er UVIRKSOM
        7 er UVIRKSOM
        8 er TIL_ARKIVERING
        9 er TIL_ARKIVERING
    }

    fun verifiserArkivertStatus() {
        1 er ARKIVERT
        2 er OPPRETT_OPPGAVE
        3 er NY_SAK
        4 er AVSLUTTES_AV_MOTPART
        5 er ARKIVERT
        6 er UVIRKSOM
        7 er UVIRKSOM
        8 er ARKIVERT
        9 er ARKIVERT
    }

    fun verifiserArkivertKall() {
        verifiserEksekvert("/api/v1/rinasaker/1/arkiver")
        verifiserEksekvert("/api/v1/rinasaker/5/arkiver")
        verifiserEksekvert("/api/v1/rinasaker/8/arkiver")
        verifiserEksekvert("/api/v1/rinasaker/9/arkiver")
    }
}

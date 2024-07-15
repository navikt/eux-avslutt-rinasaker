package no.nav.eux.avslutt.rinasaker.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*

@Entity
data class Rinasak(
    @Id
    val rinasakStatusUuid: UUID,
    val rinasakId: Int,
    @Enumerated(STRING)
    val status: Status,
    val bucType: String,
    val erSakseier: Boolean,
    val opprettetBruker: String = "ukjent",
    val opprettetTidspunkt: LocalDateTime = now(),
    val endretBruker: String = "ukjent",
    val endretTidspunkt: LocalDateTime = now(),
) {
    enum class Status {
        NY_SAK,
        DOKUMENT_SENT,
        UVIRKSOM,
        TIL_AVSLUTNING_GLOBALT,
        TIL_AVSLUTNING_LOKALT,
        TIL_AVSLUTNING_MANUELT,
        OPPRETT_OPPGAVE,
        AVSLUTTET_GLOBALT,
        AVSLUTTET_LOKALT,
        OPPGAVE_OPPRETTET,
        KAN_IKKE_AVSLUTTES,
        AVSLUTTES_AV_MOTPART
    }
}

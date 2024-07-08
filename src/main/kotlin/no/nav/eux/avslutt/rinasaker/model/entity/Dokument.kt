package no.nav.eux.avslutt.rinasaker.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*

@Entity
data class Dokument(
    @Id
    val dokumentUuid: UUID,
    val sedId: UUID,
    val sedVersjon: Int,
    val rinasakId: Int,
    val sedType: String,
    val opprettetBruker: String = "ukjent",
    val opprettetTidspunkt: LocalDateTime = now(),
    val endretBruker: String = "ukjent",
    val endretTidspunkt: LocalDateTime = now(),
)

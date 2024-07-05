package no.nav.eux.avslutt.rinasaker.persistence.repository

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface RinasakRepository : JpaRepository<Rinasak, UUID> {
    fun findByRinasakId(rinasakId: Int): Rinasak?

    fun findAllByStatusAndOpprettetTidspunktBefore(
        status: Rinasak.Status,
        opprettetTidspunkt: LocalDateTime
    ): List<Rinasak>

    fun findAllByStatus(
        status: Rinasak.Status
    ): List<Rinasak>
}

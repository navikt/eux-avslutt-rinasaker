package no.nav.eux.avslutt.rinasaker.persistence.repository

import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface RinasakRepository : JpaRepository<Rinasak, UUID> {

    fun findByRinasakId(rinasakId: Int): Rinasak?

    fun findAllByStatusAndBucType(
        status: Rinasak.Status,
        bucType: String
    ): List<Rinasak>

    fun findAllByStatusAndBucTypeAndEndretTidspunktBefore(
        status: Rinasak.Status,
        bucType: String,
        endretTidspunkt: LocalDateTime
    ): List<Rinasak>

    fun countByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(
        status: Rinasak.Status,
        fra: LocalDateTime,
        til: LocalDateTime
    ): Long

    fun findAllByStatusAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(
        status: Rinasak.Status,
        fra: LocalDateTime,
        til: LocalDateTime
    ): List<Rinasak>

    fun findAllByStatusInAndEndretTidspunktGreaterThanEqualAndEndretTidspunktLessThan(
        statuses: List<Rinasak.Status>,
        fra: LocalDateTime,
        til: LocalDateTime
    ): List<Rinasak>

    fun countByOpprettetTidspunktGreaterThanEqualAndOpprettetTidspunktLessThan(
        fra: LocalDateTime,
        til: LocalDateTime
    ): Long

    fun countByStatus(status: Rinasak.Status): Long
}

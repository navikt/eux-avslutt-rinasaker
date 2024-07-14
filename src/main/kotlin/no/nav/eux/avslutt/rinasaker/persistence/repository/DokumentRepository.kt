package no.nav.eux.avslutt.rinasaker.persistence.repository

import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface DokumentRepository : JpaRepository<Dokument, UUID> {
    fun findByRinasakId(rinasakId: Int): List<Dokument>?
    fun findBySedIdAndSedVersjon(sedId: UUID, sedVersjon: Int): Dokument?

    @Query(
        value = """
        select * from (
            select distinct on (dokument.rinasak_id) dokument.*
            from dokument, rinasak
            where dokument.rinasak_id = rinasak.rinasak_id
            and rinasak.status = :#{#status.toString()}
            order by dokument.rinasak_id, dokument.opprettet_tidspunkt desc
        ) as dokument_latest
        where dokument_latest.opprettet_tidspunkt < :date
    """,
        nativeQuery = true
    )
    fun findDokumenterBeforeDate(
        @Param("date")
        date: LocalDateTime,
        @Param("status")
        status: Rinasak.Status
    ): List<Dokument>
}

package no.nav.eux.avslutt.rinasaker.persistence.repository

import no.nav.eux.avslutt.rinasaker.model.entity.Dokument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DokumentRepository : JpaRepository<Dokument, UUID> {
    fun findByRinasakId(rinasakId: Int): List<Dokument>?
    fun findBySedIdAndSedVersjon(sedId: UUID, sedVersjon: Int): Dokument?
}

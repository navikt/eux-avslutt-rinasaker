package no.nav.eux.avslutt.rinasaker.service

import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service

@Service
class RinasakService(
    val rinasakRepository: RinasakRepository,
    val dokumentRepository: DokumentRepository,
) {

    fun sisteSedType(rinasakId: Int) =
        dokumentRepository
            .findFirstByRinasakIdOrderByOpprettetTidspunktDesc(rinasakId)
            ?.sedType
            ?: throw RuntimeException("Kunne ikke finne siste sed type for rinasak $rinasakId")

}

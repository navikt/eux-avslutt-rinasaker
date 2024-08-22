package no.nav.eux.avslutt.rinasaker.service

import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import org.springframework.stereotype.Service

@Service
class RinasakService(
    val dokumentRepository: DokumentRepository,
) {

    fun sisteSedType(rinasakId: Int) =
        dokumentRepository
            .findFirstByRinasakIdOrderByOpprettetTidspunktDesc(rinasakId)
            ?.sedType
            ?: throw RuntimeException("Kunne ikke finne siste sed type for rinasak $rinasakId")

    fun sedTyper(rinasakId: Int) =
        dokumentRepository
            .findByRinasakId(rinasakId)
            .map { it.sedType }
            .distinct()

}

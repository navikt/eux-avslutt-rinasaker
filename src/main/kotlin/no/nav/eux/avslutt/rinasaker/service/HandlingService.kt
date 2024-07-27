package no.nav.eux.avslutt.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.HANDLING_FEILET
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status.HANDLING_MANGLER
import no.nav.eux.avslutt.rinasaker.model.exception.HandlingManglerException
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class HandlingService(
    val rinasakRepository: RinasakRepository,
) {

    val log = logger {}

    fun tryHandling(
        rinasak: Rinasak,
        tilStatus: Rinasak.Status,
        endretBruker: String,
        handling: () -> Unit
    ) =
        try {
            mdc(
                rinasakId = rinasak.rinasakId,
                bucType = rinasak.bucType
            )
            handling()
            rinasakRepository.save(
                rinasak.copy(
                    status = tilStatus,
                    endretBruker = endretBruker,
                    endretTidspunkt = now()
                )
            )
        } catch (e: HandlingManglerException) {
            log.error(e) { "Handling mangler" }
            rinasakRepository.save(
                rinasak.copy(
                    status = HANDLING_MANGLER,
                    endretBruker = endretBruker,
                    endretTidspunkt = now()
                )
            )
        } catch (e: Exception) {
            log.error(e) { "Handling feilet" }
            rinasakRepository.save(
                rinasak.copy(
                    status = HANDLING_FEILET,
                    endretBruker = endretBruker,
                    endretTidspunkt = now()
                )
            )
        }
}
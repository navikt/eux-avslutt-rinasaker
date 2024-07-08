package no.nav.eux.avslutt.rinasaker.webapp

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import no.nav.eux.avslutt.rinasaker.service.SettUvirksomService
import no.nav.eux.avslutt.rinasaker.service.clearLocalMdc
import no.nav.eux.avslutt.rinasaker.service.mdc
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@Unprotected
@Validated
@RequestMapping("\${api.base-path:/api/v1}")
@RestController
class AvsluttRinasakerApi(
    val settUvirksomService: SettUvirksomService,
) {

    val log = logger {}

    @Operation(
        summary = "Avslutt rinasaker API",
        operationId = "prosess",
        description = "start prosess for sletting av usendte rinasaker",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "No content"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden"
            ),
            ApiResponse(
                responseCode = "409",
                description = "Conflict"
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        ]
    )
    @RequestMapping(
        method = [POST],
        value = ["/prosesser/{prosess}/execute"],
        produces = ["application/json"]
    )
    fun prosess(
        @Parameter(
            description = """
                Navnet på prosessen som skal startes:   
                    * `sett-uvirksom` - Markerer rinasaker som uvirksomme   
                    * `til-avslutning` - Sett rinasaker til avslutning
                    * `avslutt' - Avslutter rinasaker
                    """,
            required = true
        )
        @PathVariable("prosess")
        prosess: String
    ): ResponseEntity<Unit> {
        clearLocalMdc()
        mdc(prosess = prosess)
        log.info { "starter prosess..." }
        when (prosess) {
            "sett-uvirksom" -> settUvirksomService.settRinasakerUvirksom()
            "til-avslutning" -> log.info { "prosess ikke implementert" }
            "avslutt" -> log.info { "prosess ikke implementert" }
            else -> {
                log.error { "ukjent prosess: $prosess" }
                return ResponseEntity(BAD_REQUEST)
            }
        }
        log.info { "prosess utført" }
        return ResponseEntity(NO_CONTENT)
    }

}

package no.nav.eux.avslutt.rinasaker.webapp.dataset

import no.nav.eux.avslutt.rinasaker.kafka.model.case.*
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocument
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentMetadata
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentPayload
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.avslutt.rinasaker.webapp.common.dagerArkivering
import no.nav.eux.avslutt.rinasaker.webapp.common.dagerUvirksom
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid7
import java.time.LocalDateTime.now
import java.time.OffsetDateTime

val fbBuc04UvirksomSedIkkeF003_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 6,
            processDefinitionName = "FB_BUC_04",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = "NO:NAVAT06"))
        )
    )
)

val fbBuc04UvirksomSedIkkeF003_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_04",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000007",
            type = "H001",
            caseId = 6,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
        )
    )
)

fun DokumentRepository.case6_manipulerOpprettetTidspunkt() {
    val dokument = findBySedIdAndSedVersjon(uuid7, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerUvirksom))
    save(dokument)
}

fun RinasakRepository.case6_manipulerEndretTidspunktArkivering() {
    val dokument = findByRinasakId(6)!!
        .copy(endretTidspunkt = now().minusDays(dagerArkivering))
    save(dokument)
}
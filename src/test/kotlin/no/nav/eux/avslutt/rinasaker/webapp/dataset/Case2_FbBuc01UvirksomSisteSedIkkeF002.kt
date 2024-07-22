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
import no.nav.eux.avslutt.rinasaker.webapp.common.uuid2
import java.time.LocalDateTime.now
import java.time.OffsetDateTime

val fbBuc01UvirksomSisteSedIkkeF002_case = KafkaRinaCase(
    caseEventType = "OPEN_CASE",
    payLoad = KafkaRinaCasePayload(
        KafkaRinaCaseRestCase(
            id = 2,
            processDefinitionName = "FB_BUC_01",
            whoami = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06"),
            creator = KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = "NO:NAVAT06"))
        )
    )
)

val fbBuc01UvirksomSisteSedIkkeF002_sed = KafkaRinaDocument(
    documentEventType = "SENT_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = KafkaRinaDocumentPayload(
        KafkaRinaDocumentMetadata(
            id = "00000000000000000000000000000002",
            type = "F001",
            caseId = 2,
            versions = listOf(
                KafkaRinaDocumentVersions(id = 1)
            ),
            creationDate = OffsetDateTime.parse("2024-07-08T16:24:02+02")
        )
    )
)

fun DokumentRepository.case2_manipulerOpprettetTidspunkt() {
    val dokument = findBySedIdAndSedVersjon(uuid2, 1)!!
        .copy(opprettetTidspunkt = now().minusDays(dagerUvirksom))
    save(dokument)
}

fun RinasakRepository.case2_manipulerEndretTidspunktArkivering() {
    val dokument = findByRinasakId(2)!!
        .copy(endretTidspunkt = now().minusDays(dagerArkivering))
    save(dokument)
}

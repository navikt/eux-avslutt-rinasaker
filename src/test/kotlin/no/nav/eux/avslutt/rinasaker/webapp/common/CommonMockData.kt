package no.nav.eux.avslutt.rinasaker.webapp.common

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCaseCreator
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCaseCreatorOrganisation
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCaseWhoami
import no.nav.eux.avslutt.rinasaker.kafka.model.document.KafkaRinaDocumentVersions
import java.time.OffsetDateTime
import java.util.*
import java.util.UUID.fromString

val offsetDateTime: OffsetDateTime = OffsetDateTime.parse("2024-07-08T16:24:02+02")

const val dagerUvirksom: Long = 121
const val dagerVirksom: Long = 89

const val dagerArkivering: Long = 401

const val kafkaTopicRinaCaseEvents = "eessibasis.eux-rina-case-events-v1"
const val kafkaTopicRinaDocumentEvents = "eessibasis.eux-rina-document-events-v1"

val Int.uuid
    get(): UUID =
        fromString("00000000-0000-0000-0000-${toString().padStart(12, '0')}")

val Int.documentId
    get(): String =
        toString().padStart(32, '0')

val caseCreatorOrgNav = "NO:NAVAT06".caseCreatorOrg
val caseCreatorOrgNotNav = "NO:not-me".caseCreatorOrg
val whoamiNav = KafkaRinaCaseRestCaseWhoami(id = "NO:NAVAT06")

val String.caseCreatorOrg
    get(): KafkaRinaCaseRestCaseCreator =
        KafkaRinaCaseRestCaseCreator(KafkaRinaCaseRestCaseCreatorOrganisation(id = this))

val documentVersions = listOf(KafkaRinaDocumentVersions(id = 1))

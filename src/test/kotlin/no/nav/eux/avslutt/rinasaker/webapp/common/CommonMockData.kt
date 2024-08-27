package no.nav.eux.avslutt.rinasaker.webapp.common

import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCaseCreator
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCaseCreatorOrganisation
import no.nav.eux.avslutt.rinasaker.kafka.model.case.KafkaRinaCaseRestCaseWhoami
import java.time.OffsetDateTime
import java.util.*
import java.util.UUID.fromString

val uuid1: UUID = fromString("00000000-0000-0000-0000-000000000001")
val uuid2: UUID = fromString("00000000-0000-0000-0000-000000000002")
val uuid3: UUID = fromString("00000000-0000-0000-0000-000000000003")
val uuid4: UUID = fromString("00000000-0000-0000-0000-000000000004")
val uuid5: UUID = fromString("00000000-0000-0000-0000-000000000005")
val uuid6: UUID = fromString("00000000-0000-0000-0000-000000000006")
val uuid7: UUID = fromString("00000000-0000-0000-0000-000000000007")

val offsetDateTime: OffsetDateTime = OffsetDateTime.parse("2024-07-08T16:24:02+02")

const val dagerUvirksom: Long = 91
const val dagerVirksom: Long = 89

const val dagerArkivering: Long = 181

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

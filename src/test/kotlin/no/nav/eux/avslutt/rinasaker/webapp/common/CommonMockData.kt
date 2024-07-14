package no.nav.eux.avslutt.rinasaker.webapp.common

import java.time.OffsetDateTime
import java.util.*

val uuid1: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
val uuid2: UUID = UUID.fromString("00000000-0000-0000-0000-000000000002")
val uuid3: UUID = UUID.fromString("00000000-0000-0000-0000-000000000003")
val uuid4: UUID = UUID.fromString("00000000-0000-0000-0000-000000000004")
val uuid5: UUID = UUID.fromString("00000000-0000-0000-0000-000000000005")
val uuid6: UUID = UUID.fromString("00000000-0000-0000-0000-000000000006")

val offsetDateTime1: OffsetDateTime = OffsetDateTime.parse("2023-04-21T10:40:40.897718Z")

val offsetDateTime2: OffsetDateTime = OffsetDateTime.parse("2023-04-22T10:40:40.897718Z")

const val dagerUvirksom: Long = 91
const val dagerVirksom: Long = 89

const val kafkaTopicRinaCaseEvents = "eessibasis.eux-rina-case-events-v1"
const val kafkaTopicRinaDocumentEvents = "eessibasis.eux-rina-document-events-v1"

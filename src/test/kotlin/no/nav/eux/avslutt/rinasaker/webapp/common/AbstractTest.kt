package no.nav.eux.avslutt.rinasaker.webapp.common

import no.nav.eux.avslutt.rinasaker.Application
import no.nav.eux.avslutt.rinasaker.model.entity.Rinasak.Status
import no.nav.eux.avslutt.rinasaker.persistence.repository.DokumentRepository
import no.nav.eux.avslutt.rinasaker.persistence.repository.RinasakRepository
import no.nav.eux.avslutt.rinasaker.webapp.mock.RequestBodies
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod.POST
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals

@ActiveProfiles("test")
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EnableMockOAuth2Server
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestRestTemplate
abstract class AbstractTest {

    companion object {

        @JvmStatic
        @Container
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(
            "postgres:15-alpine"
        )

        @JvmStatic
        @Container
        val kafka = KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
        )

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.hikari.jdbc-url", postgres::getJdbcUrl)
            registry.add("spring.datasource.hikari.username", postgres::getUsername)
            registry.add("spring.datasource.hikari.password", postgres::getPassword)
            registry.add("kafka.bootstrap-servers", kafka::getBootstrapServers)
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
        }
    }

    fun httpEntity() = voidHttpEntity(mockOAuth2Server)

    @Autowired
    lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @Autowired
    lateinit var rinasakRepository: RinasakRepository

    @Autowired
    lateinit var dokumentRepository: DokumentRepository

    @Autowired
    lateinit var requestBodies: RequestBodies

    fun execute(prosess: String) {
        restTemplate
            .exchange<Void>(
                "/api/v1/prosesser/$prosess/execute",
                POST,
                httpEntity()
            )
    }

    infix fun Int.er(expected: Status) {
        assertEquals(
            actual = rinasakRepository.findByRinasakId(this)?.status,
            expected = expected,
        )
    }

    infix fun String.send(topic: Any) =
        kafkaTemplate.send(this, topic)

    fun skrivUtEksterneKall() {
        println("Følgende requests ble utført i prosessen:")
        requestBodies.forEach { println("Path: ${it.key}, body: ${it.value}") }
    }

    fun verifiserEksekvert(uri: String) {
        assertThat(requestBodies[uri]).isNotNull()
    }
}

package no.nav.eux.avslutt.rinasaker.integration

import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@EnableOAuth2Client(cacheEnabled = true)
@Configuration
class IntegrationConfig {

    @Bean
    fun euxRinaTerminatorApiRestClient(components: RestClientComponents) =
        components prefixBy "eux-rina-terminator-api"

    @Bean
    fun restClientComponents(
        restClientBuilder: RestClient.Builder,
        clientConfigurationProperties: ClientConfigurationProperties,
        oAuth2AccessTokenService: OAuth2AccessTokenService
    ) = RestClientComponents(
        restClientBuilder = restClientBuilder,
        clientConfigurationProperties = clientConfigurationProperties,
        oAuth2AccessTokenService = oAuth2AccessTokenService
    )

    infix fun RestClientComponents.prefixBy(appName: String): RestClient {
        val clientProperties: ClientProperties = clientConfigurationProperties
            .registration["$appName-credentials"]
            ?: throw RuntimeException("could not find oauth2 client config for $appName-credentials")
        return restClientBuilder
            .requestInterceptor { request, body, execution ->
                val response = oAuth2AccessTokenService.getAccessToken(clientProperties)
                request.headers.setBearerAuth(response.access_token!!)
                execution.execute(request, body)
            }
            .build()
    }
}

data class RestClientComponents(
    val restClientBuilder: RestClient.Builder,
    val clientConfigurationProperties: ClientConfigurationProperties,
    val oAuth2AccessTokenService: OAuth2AccessTokenService
)

package org.jab.microservices.wintergames1.router;

import org.jab.microservices.wintergames1.model.MyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081)
@ActiveProfiles("test")
@DirtiesContext
public class IntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    public void setupWireMock() throws Exception {

        final String pcfRespponse = getResourceAsString("pcf_info_response.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(pcfRespponse)));

        final String bluemixResponse = getResourceAsString("bluemix_info_response.json");
        stubFor(get(urlEqualTo("/v2/infob"))
                .willReturn(okJson(bluemixResponse)));
    }

    @Disabled
    @Test
    public void Given_a_request_When_both_cloudFoundry_installations_has_expected_versions_Then_return_true() {

        testClient.get()
                .uri("/api/version")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(true));
    }

    @Disabled
    @Test
    public void Given_a_request_When_both_cloudFoundry_installations_has_expected_versions_Then_return_true_And_processed_in_sequence() {

        testClient.get()
                .uri("/api/version/sequence")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(true));
    }

    @Test
    public void exampleTestNoOK() throws Exception {
        testClient.get()
                .uri("/api/versionKKK")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("timestamp").isNotEmpty()
                .jsonPath("path").isEqualTo("/api/versionKKK")
                .jsonPath("error").isEqualTo("Not Found")
                .jsonPath("message").isEqualTo("No matching handler");
    }

    public static String getResourceAsString(final String pathToFile) throws IOException {
        return copyToString(new ClassPathResource(pathToFile).getInputStream(), defaultCharset());
    }

}

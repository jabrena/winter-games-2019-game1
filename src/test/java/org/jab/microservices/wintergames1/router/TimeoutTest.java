package org.jab.microservices.wintergames1.router;

import org.jab.microservices.wintergames1.model.MyResponse;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081)
@ActiveProfiles("test")
@DirtiesContext
public class TimeoutTest {

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    public void setupWireMock() throws Exception {

        final String pcfRespponse = getResourceAsString("pcf_info_response.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(pcfRespponse).withFixedDelay(5000)));

        final String bluemixResponse = getResourceAsString("bluemix_info_response.json");
        stubFor(get(urlEqualTo("/v2/infob"))
                .willReturn(okJson(bluemixResponse)));
    }

    @Test
    public void Given_a_request_When_any_provider_generate_timeout_Then_return_false() {


        WebTestClient client = testClient
            .mutate()
            .responseTimeout(Duration.ofMillis(30000))
            .build();

        client.get()
                .uri("/api/version/sequence")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(false));
    }

    public static String getResourceAsString(final String pathToFile) throws IOException {
        return copyToString(new ClassPathResource(pathToFile).getInputStream(), defaultCharset());
    }

}

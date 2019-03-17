package org.jab.microservices.wintergames1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8082)
@ActiveProfiles("test")
@DirtiesContext
public class BluemixInfoAdapterTest {

    @Autowired
    private BluemixInfoAdapter bluemixInfoAdapter;

    public static String getResourceAsString(final String pathToFile) throws IOException {
        return copyToString(new ClassPathResource(pathToFile).getInputStream(), defaultCharset());
    }

    @Test
    public void Given_a_request_When_call_bluemix_Then_return_true_if_expected_version() throws Exception {

        final String response = getResourceAsString("bluemix_info_success_response.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(response)));

        StepVerifier.create(bluemixInfoAdapter.getVersion())
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Test
    public void Given_a_request_When_call_bluemix_Then_return_false_if_version_is_not_expected() throws Exception {

        final String response = getResourceAsString("bluemix_info_success_response_upper_version.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(response)));

        StepVerifier.create(bluemixInfoAdapter.getVersion())
                .expectNext(false)
                .expectComplete()
                .verify();
    }

    @Test
    public void Given_a_request_When_call_bluemix_Then_return_false_if_timeout() throws Exception {

        final String response = getResourceAsString("bluemix_info_success_response.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(response).withFixedDelay(5000)));

        StepVerifier.create(bluemixInfoAdapter.getVersion())
                .expectNext(false)
                .expectComplete()
                .verify();
    }

}
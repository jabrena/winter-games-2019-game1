package org.jab.microservices.wintergames1.router;

import org.jab.microservices.wintergames1.model.MyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class SystemTests {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void Given_a_request_When_both_cloudFoundry_installations_has_expected_versions_Then_return_true() throws Exception {
        testClient.get()
            .uri("/api/version")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(MyResponse.class)
            .isEqualTo(new MyResponse(true));
    }

    @Test
    public void Given_a_request_When_both_cloudFoundry_installations_has_expected_versions_Then_return_true_And_processed_in_sequence() throws Exception {
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
}

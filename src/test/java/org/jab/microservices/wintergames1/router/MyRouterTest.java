package org.jab.microservices.wintergames1.router;

import org.jab.microservices.wintergames1.handler.MyHandler;
import org.jab.microservices.wintergames1.handler.MyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyRouterTest {

    @SpyBean
    private MyHandler handler;

    @Autowired
    private WebTestClient testClient;

    @Test
    public void Given_a_request_When_both_cloudFoundry_installations_has_expected_versions_Then_return_true() {

        given(handler.areVersionsOKParallel()).willReturn(Mono.just(true));

        testClient.get()
                .uri("/api/version")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(true));
    }

    @Test
    public void Given_a_request_When_both_cloudFoundry_installations_has_expected_versions_Then_return_true_And_processed_in_sequence() {

        given(handler.areVersionsOKSequence()).willReturn(Mono.just(true));

        testClient.get()
                .uri("/api/version/sequence")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(true));
    }

    //TODO Improve this kind of tests
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

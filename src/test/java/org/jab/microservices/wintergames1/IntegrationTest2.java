package org.jab.microservices.wintergames1;

import org.jab.microservices.wintergames1.model.MyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.jab.microservices.wintergames1.TestUtils.getResourceAsString;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081)
@ActiveProfiles("test")
public class IntegrationTest2 {

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    public void setupWireMock() throws Exception {

        final String pcfRespponse = getResourceAsString("pcf_info_response.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(pcfRespponse)));
        /*
        final String bluemixResponse = getResourceAsString("bluemix_info_response.json");
        stubFor(get(urlEqualTo("/v2/info"))
                .willReturn(okJson(bluemixResponse)));
        */
    }

    @Test
    public void exampleTest() throws Exception {

        testClient.get()
                .uri("/api/version")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(true));
    }


    @Test
    public void exampleTest2() throws Exception {

        testClient.get()
                .uri("/api/version2")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(false));
    }
}

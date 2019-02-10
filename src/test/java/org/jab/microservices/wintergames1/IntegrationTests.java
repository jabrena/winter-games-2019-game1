package org.jab.microservices.wintergames1;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void exampleTest() throws Exception {
        testClient.get()
                .uri("/api/version")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MyResponse.class)
                .isEqualTo(new MyResponse(false));
    }

}

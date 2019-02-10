package org.jab.microservices.wintergames1;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class MyHandler {

    public Mono<ServerResponse> getVersion(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(new MyResponse(false)), MyResponse.class);
    }
}

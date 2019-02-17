package org.jab.microservices.wintergames1;

import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.model.InfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class MyHandler {

    private final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.run.pivotal.io")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(logRequest())
            .build();

    public Mono<ServerResponse> getVersion(ServerRequest serverRequest) {
        log.info("Executing GetVersion");
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(new MyResponse(true)), MyResponse.class);
    }

    /*
    public Mono<ServerResponse> getVersion2(ServerRequest serverRequest) {
        log.info("Executing GetVersion2");
        return fromClientResponse(client.get()
                .uri("/v2/info")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ClientResponse.class));
                //.flatMap(response -> response.toEntity(InfoResponse.class) );
    }
    */

    public Mono<ServerResponse> getVersion2(ServerRequest request) {
        log.info("Executing GetVersion2");
        return request.principal().flatMap((principal) -> {
            return client.get()
                    .uri("/v2/info")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .flatMap((ClientResponse mapper) -> {
                        return ServerResponse.status(mapper.statusCode())
                                .headers(c -> mapper.headers().asHttpHeaders().forEach((name, value) -> c.put(name, value)))
                                .body(mapper.bodyToMono(InfoResponse.class), InfoResponse.class);
                    });
        });
    }


    // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private static Mono<ServerResponse> fromClientResponse(ClientResponse clientResponse){
        return ServerResponse.status(clientResponse.statusCode())
                .headers(headerConsumer -> clientResponse.headers().asHttpHeaders().forEach(headerConsumer::addAll))
                .body(clientResponse.bodyToMono(String.class), String.class);
    }
}

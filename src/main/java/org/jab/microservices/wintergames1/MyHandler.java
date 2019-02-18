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
            //TODO move dynamic configuration
            //.baseUrl("http://localhost:8081")
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

    private Mono<InfoResponse> getCFInfo() {
        return client.get()
                .uri("/v2/info")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(InfoResponse.class);

    }

    private static boolean isVersionOK(InfoResponse infoResponse) {
        return infoResponse.getApiVersion().equals("2.131.0");
    }


    public Mono<ServerResponse> getVersion2(ServerRequest serverRequest) {
        log.info("Executing GetVersion2");
        return getCFInfo()
                .filter(MyHandler::isVersionOK)
                .flatMap(infoResponse -> Mono.just(true))
                .switchIfEmpty(Mono.just(false))
                .flatMap(versionIsOK -> {
                    return ServerResponse
                            .ok()
                            .contentType(APPLICATION_JSON)
                            .body(Mono.just(new MyResponse(versionIsOK)), MyResponse.class);
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

}
package org.jab.microservices.wintergames1.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.CloudFoundryProviders;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.jab.microservices.wintergames1.config.Host;
import org.jab.microservices.wintergames1.model.CloudFoundryInfoResponse;
import org.jab.microservices.wintergames1.model.MyResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Predicate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@AllArgsConstructor
public class MyHandler {

    private final GlobalConfiguration config;

    private Host getHostByProvider(CloudFoundryProviders provider) {
        return config.getHosts().stream()
                .filter(x -> x.getId().equals(provider))
                .findFirst()
                .get();
    }

    private WebClient initWebClient(CloudFoundryProviders provider) {

        final Host host = this.getHostByProvider(provider);

        LOGGER.info("Configuring id: {} with address: {}", host.getId(), host.getAddress());
        LOGGER.info("Connection timeout: {}, Read timeout: {}, Write timeout: {}", host.getConntimeout(), host.getReadtimeout(), host.getWritetimeout());

        return WebClient
                .builder()
                .baseUrl(host.getAddress())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
    }

    private Mono<Boolean> makeCall(CloudFoundryProviders provider) {

        final Host host = getHostByProvider(provider);

        final Predicate<CloudFoundryInfoResponse> versionOK =
                (response) -> response.getApiVersion().equals(host.getVersion());

        CloudFoundryInfoResponse fallback = CloudFoundryInfoResponse.builder().apiVersion("BAD").build();
            return this.initWebClient(provider)
                    .get()
                    .uri(host.getResource())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(CloudFoundryInfoResponse.class)
                    .timeout(Duration.ofMillis(host.getConntimeout()))
                    .onErrorReturn(fallback)
                    //.onErrorMap(throwable -> new RuntimeException("my exception"))
                    .log()
                    .filter(versionOK)
                    .flatMap(infoResponse -> Mono.just(true))
                    .switchIfEmpty(Mono.just(false));
    }

    private Mono<Boolean> getPCFInfo() {
        return makeCall(CloudFoundryProviders.PFC);
    }

    private Mono<Boolean> getBluemixInfo() {
        return makeCall(CloudFoundryProviders.BLUEMIX);
    }

    public Mono<Boolean> areVersionsOKSequence() {
        final Mono<Boolean> isPCFVersionOK = getPCFInfo();
        final Mono<Boolean> isBlueMixVersionOK = getBluemixInfo();

        return isPCFVersionOK
                .flatMap(versionIsOK -> isBlueMixVersionOK
                        .flatMap(versionIsOK2 -> Mono.just(versionIsOK && versionIsOK2)));
    }

    public Mono<Boolean> areVersionsOKParallel(){
        final Mono<Boolean> isPCFVersionOK = getPCFInfo();
        final Mono<Boolean> isBlueMixVersionOK = getBluemixInfo();

        //TODO Simplify this syntax
        return isPCFVersionOK.mergeWith(isBlueMixVersionOK)
            .filter(aBoolean -> {
                return aBoolean;
            })
            .collectList().map(booleans -> {
                return booleans.size() == 2;
        });
    }

    public Mono<ServerResponse> getVersionParallel(ServerRequest serverRequest) {
        return areVersionsOKParallel().flatMap(areOK -> {
            return ServerResponse
                    .ok()
                    .contentType(APPLICATION_JSON)
                    .body(Mono.just(new MyResponse(areOK)), MyResponse.class);
        });
    }

    public Mono<ServerResponse> getVersionSequence(ServerRequest serverRequest) {
        return areVersionsOKSequence().flatMap(areOK -> {
            return ServerResponse
                    .ok()
                    .contentType(APPLICATION_JSON)
                    .body(Mono.just(new MyResponse(areOK)), MyResponse.class);
        });
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOGGER.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
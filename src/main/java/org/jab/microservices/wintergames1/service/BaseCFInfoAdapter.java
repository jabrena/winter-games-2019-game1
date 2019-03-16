package org.jab.microservices.wintergames1.service;

import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.CloudFoundryProvider;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.jab.microservices.wintergames1.config.Host;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class BaseCFInfoAdapter {

    private Host getHostByProvider(GlobalConfiguration config, CloudFoundryProvider provider) {
        return config.getHosts().stream()
                .filter(x -> x.getId().equals(provider))
                .findFirst()
                .get();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOGGER.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private WebClient initWebClient(GlobalConfiguration config, CloudFoundryProvider provider) {

        final Host host = this.getHostByProvider(config, provider);

        LOGGER.info("Configuring id: {} with address: {}", host.getId(), host.getAddress());
        LOGGER.info("Connection timeout: {} ", host.getConntimeout());

        return WebClient
                .builder()
                .baseUrl(host.getAddress())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
    }

    protected Mono<Boolean> makeCall(GlobalConfiguration config, CloudFoundryProvider provider) {

        final Host host = getHostByProvider(config, provider);

        final Predicate<CloudFoundryInfoResponse> versionOK =
                (response) -> {
                        LOGGER.info("Api version: {} Expected: {}", response.getApiVersion(), host.getVersion());
                        return response.getApiVersion().equals(host.getVersion());
                };

        CloudFoundryInfoResponse fallback = CloudFoundryInfoResponse.builder().apiVersion("FALLBACK_VERSION").build();

        return this.initWebClient(config, provider)
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
                .switchIfEmpty(Mono.just(false))
                .map(Function.identity());
    }

}

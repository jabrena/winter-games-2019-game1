package org.jab.microservices.wintergames1.handler;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.CloudFoundryProviders;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.jab.microservices.wintergames1.config.Host;
import org.jab.microservices.wintergames1.model.CloudFoundryInfoResponse;
import org.jab.microservices.wintergames1.model.MyCustomClientException;
import org.jab.microservices.wintergames1.model.MyResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.function.Predicate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class MyHandler {

    private final GlobalConfiguration config;

    public MyHandler(GlobalConfiguration config) {
        this.config = config;
    }

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

        final TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, host.getConntimeout())
                .noProxy()
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(host.getReadtimeout()))
                        .addHandlerLast(new WriteTimeoutHandler(host.getWritetimeout())));

        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .baseUrl(host.getAddress())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
    }

    private Mono<Boolean> makeCall(WebClient client, CloudFoundryProviders providers) {

        final Host host = getHostByProvider(providers);
        final Predicate<CloudFoundryInfoResponse> versionOK =
                (response) -> response.getApiVersion().equals(host.getVersion());

        return client.get()
                .uri(host.getResource())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .bodyToMono(CloudFoundryInfoResponse.class)
                .filter(versionOK)
                .flatMap(infoResponse -> Mono.just(true))
                .switchIfEmpty(Mono.just(false));
    }

    private Mono<Boolean> getPCFInfo() {
        WebClient client1 = this.initWebClient(CloudFoundryProviders.PFC);

        return makeCall(client1, CloudFoundryProviders.PFC);
    }

    private Mono<Boolean> getBluemixInfo() {
        WebClient client2 = this.initWebClient(CloudFoundryProviders.BLUEMIX);

        return makeCall(client2, CloudFoundryProviders.BLUEMIX);
    }

    public Mono<Boolean> areVersionsOKSequence() {
        final Mono<Boolean> isPCFVersionOK = getPCFInfo();
        final Mono<Boolean> isBlueMixVersionOK = getBluemixInfo();

        return isPCFVersionOK
                .flatMap(versionIsOK -> {
                    return isBlueMixVersionOK
                            .flatMap(versionIsOK2 -> {
                                return Mono.just(versionIsOK && versionIsOK2);
                            });
                });
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
package org.jab.microservices.wintergames1.controller;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.jab.microservices.wintergames1.model.InfoResponse;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class MyHandler {

    private final WebClient client;

    /*
    ReactorClientHttpConnector connector =
            new ReactorClientHttpConnector(options ->
                    options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000));
            WebClient webClient = WebClient.builder().clientConnector(connector).build();
     */

    public MyHandler(GlobalConfiguration config) {

        final String host = config.getHost();
        final Integer conntimeout = config.getConntimeout();
        final Integer readtimeout = config.getReadtimeout();
        final Integer writetimeout = config.getWritetimeout();
        log.info("Host: {}", host);
        log.info("Connection Timeout: {}", conntimeout);
        log.info("Read Timeout: {}", readtimeout);
        log.info("Write Timeout: {}", writetimeout);

        final TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, conntimeout)
                .noProxy()
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(readtimeout))
                        .addHandlerLast(new WriteTimeoutHandler(writetimeout)));
        this.client = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .baseUrl(host)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
    }

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
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new MyCustomServerException())
                )
                .bodyToMono(InfoResponse.class);
    }

    private static boolean isVersionOK(InfoResponse infoResponse) {
        return infoResponse.getApiVersion().equals("2.131.0");
    }


    public Mono<ServerResponse> getVersion2(ServerRequest serverRequest) {
        log.info("Executing GetVersion2");
        return getCFInfo()
                //.onErrorReturn();
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
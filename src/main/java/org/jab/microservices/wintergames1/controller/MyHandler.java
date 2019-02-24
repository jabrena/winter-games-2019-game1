package org.jab.microservices.wintergames1.controller;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.jab.microservices.wintergames1.model.BluemixInfoResponse;
import org.jab.microservices.wintergames1.model.MyResponse;
import org.jab.microservices.wintergames1.model.PCFInfoResponse;
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

    public static final String PCF_ENDPOINT = "https://api.run.pivotal.io";
    public static final String PCF_LATEST_VERSION = "2.131.0";

    public static final String BLUEMIX_ENDPOINT = "https://api.ng.bluemix.net";
    public static final String BLUEMIX_LATEST_VERSION = "2.106.0";

    private final WebClient client1;
    private final WebClient client2;

    public MyHandler(GlobalConfiguration config) {

        final String pcf_host = config.getPcf_host();
        final Integer pcf_conntimeout = config.getPcf_conntimeout();
        final Integer pcf_readtimeout = config.getPcf_readtimeout();
        final Integer pcf_writetimeout = config.getPcf_writetimeout();
        log.info("Host: {}", pcf_host);
        log.info("Connection Timeout: {}", pcf_conntimeout);
        log.info("Read Timeout: {}", pcf_readtimeout);
        log.info("Write Timeout: {}", pcf_writetimeout);

        final String bluemix_host = config.getBluemix_host();
        final Integer bluemix_conntimeout = config.getBluemix_conntimeout();
        final Integer bluemix_readtimeout = config.getBluemix_readtimeout();
        final Integer bluemix_writetimeout = config.getBluemix_writetimeout();
        log.info("Host: {}", bluemix_host);
        log.info("Connection Timeout: {}", bluemix_conntimeout);
        log.info("Read Timeout: {}", bluemix_readtimeout);
        log.info("Write Timeout: {}", bluemix_writetimeout);

        final TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, pcf_conntimeout)
                .noProxy()
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(pcf_readtimeout))
                        .addHandlerLast(new WriteTimeoutHandler(pcf_writetimeout)));

        this.client1 = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .baseUrl(pcf_host)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();

        final TcpClient tcpClient2 = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, bluemix_conntimeout)
                .noProxy()
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(bluemix_readtimeout))
                        .addHandlerLast(new WriteTimeoutHandler(bluemix_writetimeout)));

        this.client2 = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient2)))
                .baseUrl(bluemix_host)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
    }

    private Mono<PCFInfoResponse> getPCFInfo() {
        return client1.get()
                .uri("/v2/info")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .bodyToMono(PCFInfoResponse.class);
    }

    private Mono<BluemixInfoResponse> getBluemixInfo() {
        return client2.get()
                .uri("/v2/info")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new MyCustomClientException())
                )
                .bodyToMono(BluemixInfoResponse.class);
    }

    private static boolean isVersionOK(PCFInfoResponse infoResponse) {
        log.info("{} {}", PCF_LATEST_VERSION, infoResponse.getApiVersion());
        return infoResponse.getApiVersion().equals(PCF_LATEST_VERSION);
    }

    private static boolean isVersionOK2(BluemixInfoResponse infoResponse) {
        log.info("{} {}", BLUEMIX_LATEST_VERSION, infoResponse.getApiVersion());
        return infoResponse.getApiVersion().equals(BLUEMIX_LATEST_VERSION);
    }

    public Mono<ServerResponse> getVersion(ServerRequest serverRequest) {
        log.info("Executing GetVersion");
        return getPCFInfo()
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

    public Mono<ServerResponse> getVersion2(ServerRequest serverRequest) {
        log.info("Executing GetVersion");
        return getPCFInfo()
                .filter(MyHandler::isVersionOK)
                .flatMap(infoResponse -> Mono.just(true))
                .switchIfEmpty(Mono.just(false))
                .flatMap(versionIsOK -> {
                    return getBluemixInfo()
                            .filter(MyHandler::isVersionOK2)
                            .flatMap(infoResponse -> Mono.just(true))
                            .switchIfEmpty(Mono.just(false))
                            .flatMap(versionIsOK2 -> {
                                if(versionIsOK && versionIsOK2) {
                                    return ServerResponse
                                            .ok()
                                            .contentType(APPLICATION_JSON)
                                            .body(Mono.just(new MyResponse(true)), MyResponse.class);
                                } else {
                                    return ServerResponse
                                            .ok()
                                            .contentType(APPLICATION_JSON)
                                            .body(Mono.just(new MyResponse(false)), MyResponse.class);
                                }
                            });
                });
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
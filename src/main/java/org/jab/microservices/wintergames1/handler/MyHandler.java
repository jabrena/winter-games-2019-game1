package org.jab.microservices.wintergames1.handler;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.jab.microservices.wintergames1.model.BluemixInfoResponse;
import org.jab.microservices.wintergames1.model.MyCustomClientException;
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

    public static final String PCF_LATEST_VERSION = "2.131.0";
    public static final String BLUEMIX_LATEST_VERSION = "2.106.0";

    private final WebClient client1;
    private final WebClient client2;

    public MyHandler(GlobalConfiguration config) {


        final String pcf_host = config.getHosts().get(0).getAddress();
        final Integer pcf_conntimeout = config.getHosts().get(0).getConntimeout();
        final Integer pcf_readtimeout = config.getHosts().get(0).getReadtimeout();
        final Integer pcf_writetimeout = config.getHosts().get(0).getWritetimeout();
        LOGGER.info("Host: {}", pcf_host);
        LOGGER.info("Connection Timeout: {}", pcf_conntimeout);
        LOGGER.info("Read Timeout: {}", pcf_readtimeout);
        LOGGER.info("Write Timeout: {}", pcf_writetimeout);

        final String bluemix_host = config.getHosts().get(1).getAddress();
        final Integer bluemix_conntimeout = config.getHosts().get(1).getConntimeout();
        final Integer bluemix_readtimeout = config.getHosts().get(1).getReadtimeout();
        final Integer bluemix_writetimeout = config.getHosts().get(1).getWritetimeout();
        LOGGER.info("Host: {}", bluemix_host);
        LOGGER.info("Connection Timeout: {}", bluemix_conntimeout);
        LOGGER.info("Read Timeout: {}", bluemix_readtimeout);
        LOGGER.info("Write Timeout: {}", bluemix_writetimeout);

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

    private Mono<Boolean> getPCFInfo() {
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
            .bodyToMono(PCFInfoResponse.class)
            .filter(MyHandler::isVersionOK)
            .flatMap(infoResponse -> Mono.just(true))
            .switchIfEmpty(Mono.just(false));
    }

    private Mono<Boolean> getBluemixInfo() {
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
            .bodyToMono(BluemixInfoResponse.class)
            .filter(MyHandler::isVersionOK2)
            .flatMap(infoResponse -> Mono.just(true))
            .switchIfEmpty(Mono.just(false));
    }

    private static boolean isVersionOK(PCFInfoResponse infoResponse) {
        LOGGER.info("{} {}", PCF_LATEST_VERSION, infoResponse.getApiVersion());
        return infoResponse.getApiVersion().equals(PCF_LATEST_VERSION);
    }

    private static boolean isVersionOK2(BluemixInfoResponse infoResponse) {
        LOGGER.info("{} {}", BLUEMIX_LATEST_VERSION, infoResponse.getApiVersion());
        return infoResponse.getApiVersion().equals(BLUEMIX_LATEST_VERSION);
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
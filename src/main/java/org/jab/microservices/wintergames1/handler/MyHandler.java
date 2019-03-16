package org.jab.microservices.wintergames1.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.service.BluemixInfoAdapter;
import org.jab.microservices.wintergames1.service.PCFInfoAdapter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@AllArgsConstructor
public class MyHandler {

    private final PCFInfoAdapter pcfInfoAdapter;
    private final BluemixInfoAdapter bluemixInfoAdapter;

    public Mono<Boolean> areVersionsOKSequence() {
        final Mono<Boolean> isPCFVersionOK = pcfInfoAdapter.getVersion();
        final Mono<Boolean> isBlueMixVersionOK = bluemixInfoAdapter.getVersion();

        return isPCFVersionOK
                .flatMap(versionIsOK -> isBlueMixVersionOK
                        .flatMap(versionIsOK2 -> Mono.just(versionIsOK && versionIsOK2)));
    }

    public Mono<Boolean> areVersionsOKParallel(){
        final Mono<Boolean> isPCFVersionOK = pcfInfoAdapter.getVersion();
        final Mono<Boolean> isBlueMixVersionOK = bluemixInfoAdapter.getVersion();

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

}
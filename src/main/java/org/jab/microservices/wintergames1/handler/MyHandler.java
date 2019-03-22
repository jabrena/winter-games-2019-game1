package org.jab.microservices.wintergames1.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.service.BluemixInfoAdapter;
import org.jab.microservices.wintergames1.service.PCFInfoAdapter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@AllArgsConstructor
public class MyHandler {

    private final PCFInfoAdapter pcfInfoAdapter;
    private final BluemixInfoAdapter bluemixInfoAdapter;

    public Mono<Boolean> isPCFVersionOK(Mono<Object> voidMono){
        return pcfInfoAdapter.getVersion();
    }

    public Mono<Boolean> andBlueMixVersionIsOK(Mono<Boolean> isPCFVersionOKMono){
        return bluemixInfoAdapter
            .getVersion()
            .flatMap(booleanMono -> isPCFVersionOKMono.flatMap(aBoolean -> Mono.just(booleanMono && aBoolean)));
    }

    public Mono<Boolean> areVersionsOKSequence() {
        return Mono.empty()
            .transform(this::isPCFVersionOK)
            .transform(this::andBlueMixVersionIsOK);
    }

    private boolean onlyTrue(Boolean aBoolean){
        return aBoolean == true;
    }

    private boolean weHaveTwoElements(List<Boolean> elements){
        return elements.size() == 2;
    }

    public Mono<Boolean> areVersionsOKParallel(){
        return pcfInfoAdapter.getVersion()
            .mergeWith(bluemixInfoAdapter.getVersion())
            .filter(this::onlyTrue)
            .collectList()
            .map(this::weHaveTwoElements);
    }

    private Mono<MyResponse> toResponse(Mono<Boolean> areOKMono) {
        return areOKMono.flatMap(aBoolean -> Mono.just(new MyResponse(aBoolean)));
    }

    private Mono<ServerResponse> toServerResponse(Mono<MyResponse> responseMono) {
        return responseMono.transform(response -> ServerResponse
            .ok()
            .contentType(APPLICATION_JSON)
            .body(responseMono, MyResponse.class));
    }

    private Mono<ServerResponse> process(Mono<Boolean> booleanMono){
        return booleanMono
            .transform(this::toResponse)
            .transform(this::toServerResponse);
    }

    public Mono<ServerResponse> getVersionParallel(ServerRequest serverRequest) {
        return pcfInfoAdapter.getVersion()
            .mergeWith(bluemixInfoAdapter.getVersion())
            .filter(this::onlyTrue)
            .collectList()
            .map(this::weHaveTwoElements)
            .transform(this::process);
    }

    public Mono<ServerResponse> getVersionSequence(ServerRequest serverRequest) {
        return Mono.empty()
            .transform(this::isPCFVersionOK)
            .transform(this::andBlueMixVersionIsOK)
            .transform(this::process);
    }

}
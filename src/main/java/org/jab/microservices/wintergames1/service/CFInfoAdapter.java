package org.jab.microservices.wintergames1.service;

import reactor.core.publisher.Mono;

public interface CFInfoAdapter {

    Mono<Boolean> getVersion();

}

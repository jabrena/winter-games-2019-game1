package org.jab.microservices.wintergames1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@EnableWebFlux
@Configuration
public class WebConfig {

    @Bean
    public RouterFunction<ServerResponse> myRoutes (MyHandler myHandler) {
        return RouterFunctions.route(
                GET("/api/version")
                        .and(accept(APPLICATION_JSON)), myHandler::getVersion);
    }
}
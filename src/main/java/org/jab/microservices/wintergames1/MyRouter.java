package org.jab.microservices.wintergames1;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

public class MyRouter {

    public RouterFunction<ServerResponse> peopleRoutes(MyHandler myHandler) {
        return RouterFunctions.route(
                GET("/api/version")
                        .and(accept(APPLICATION_JSON)), myHandler::getVersion);
    }
}

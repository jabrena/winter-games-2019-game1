package org.jab.microservices.wintergames1.router;

import org.jab.microservices.wintergames1.handler.ErrorHandler;
import org.jab.microservices.wintergames1.handler.MyHandler;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class MyRouter {

    public RouterFunction<ServerResponse> myroutes(MyHandler myHandler, ErrorHandler myErrorHandler) {

        return route(GET("/api/version").and(accept(APPLICATION_JSON)), myHandler::getVersionParallel)
                .andRoute(GET("/api/version/sequence").and(accept(APPLICATION_JSON)), myHandler::getVersionSequence);
                //.andOther(route(all(), myErrorHandler::notFund));
    }
}

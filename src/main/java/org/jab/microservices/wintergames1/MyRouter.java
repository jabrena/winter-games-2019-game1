package org.jab.microservices.wintergames1;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class MyRouter {

    public RouterFunction<ServerResponse> myroutes(MyHandler myHandler, ErrorHandler myErrorHandler) {

        return route(GET("/api/version").and(accept(APPLICATION_JSON)), myHandler::getVersion)
                .andRoute(GET("/api/version2").and(accept(APPLICATION_JSON)), myHandler::getVersion2);
                //.andOther(route(all(), myErrorHandler::notFund));
    }
}

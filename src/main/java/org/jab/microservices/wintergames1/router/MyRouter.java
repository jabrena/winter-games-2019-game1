package org.jab.microservices.wintergames1.router;

import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.handler.ErrorHandler;
import org.jab.microservices.wintergames1.handler.MyHandler;
import org.jab.microservices.wintergames1.model.MyCustomClientException;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
public class MyRouter {

    public RouterFunction<ServerResponse> myroutes(MyHandler myHandler, ErrorHandler myErrorHandler) {

        return route(GET("/api/version").and(accept(APPLICATION_JSON)), myHandler::getVersionParallel)
                .andRoute(GET("/api/version/sequence").and(accept(APPLICATION_JSON)), myHandler::getVersionSequence);
                /*
                .filter((request, next) -> {
                    try {
                        return next.handle(request);
                    } catch (Exception e) {
                        LOGGER.error("An error occured", e);
                        return ServerResponse.notFound().build();
                    }
                });
                */
    }

/*
    private HandlerFilterFunction<ServerResponse, ServerResponse> dataNotFoundToBadRequest() {
        return (request, next) -> {
            try {
                return next.handle(request);
            } catch (Exception e) {
                LOGGER.error("An error occured", e);

                //when (ex) {
                //    is NotFoundException -> ServerResponse.notFound().build()
                //else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                //}

                return ServerResponse.notFound().build();
            }

        }
    }
*/
}

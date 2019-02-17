package org.jab.microservices.wintergames1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFlux
@Configuration
public class WebConfig {

    @Bean
    MyHandler myHandler() {
        return new MyHandler();
    }

    @Bean
    ErrorHandler myErrorHandler() {
        return new ErrorHandler();
    }

    @Bean
    public RouterFunction<ServerResponse> myRoutes (MyHandler myHandler, ErrorHandler myErrorHandler) {
        return new MyRouter().myroutes(myHandler, myErrorHandler);
    }
}
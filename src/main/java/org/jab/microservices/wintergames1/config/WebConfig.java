package org.jab.microservices.wintergames1.config;

import org.jab.microservices.wintergames1.controller.ErrorHandler;
import org.jab.microservices.wintergames1.controller.MyHandler;
import org.jab.microservices.wintergames1.controller.MyRouter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFlux
@Configuration
public class WebConfig {

    @Bean
    @ConfigurationProperties(prefix = "location")
    GlobalConfiguration config(){
        return new GlobalConfiguration();
    }

    @Bean
    MyHandler myHandler (GlobalConfiguration config) {
        return new MyHandler(config);
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
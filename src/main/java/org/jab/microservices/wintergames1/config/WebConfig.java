package org.jab.microservices.wintergames1.config;

import org.jab.microservices.wintergames1.handler.ErrorHandler;
import org.jab.microservices.wintergames1.handler.MyHandler;
import org.jab.microservices.wintergames1.router.MyRouter;
import org.jab.microservices.wintergames1.service.BluemixInfoAdapter;
import org.jab.microservices.wintergames1.service.BluemixInfoAdapterImpl;
import org.jab.microservices.wintergames1.service.PCFInfoAdapter;
import org.jab.microservices.wintergames1.service.PCFInfoAdapterImpl;
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
    PCFInfoAdapter pcfInfoAdapter(GlobalConfiguration config) {
        return new PCFInfoAdapterImpl(config);
    }

    @Bean
    BluemixInfoAdapter bluemixInfoAdapter(GlobalConfiguration config) {
        return new BluemixInfoAdapterImpl(config);
    }

    @Bean
    MyHandler myHandler (PCFInfoAdapter pcfInfoAdapter, BluemixInfoAdapter bluemixInfoAdapter) {
        return new MyHandler(pcfInfoAdapter , bluemixInfoAdapter);
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
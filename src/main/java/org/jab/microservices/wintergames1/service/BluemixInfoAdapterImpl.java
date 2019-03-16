package org.jab.microservices.wintergames1.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jab.microservices.wintergames1.config.CloudFoundryProvider;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("bluemix")
@AllArgsConstructor
public class BluemixInfoAdapterImpl extends BaseCFInfoAdapter implements BluemixInfoAdapter {

    private final CloudFoundryProvider provider = CloudFoundryProvider.BLUEMIX;
    private final GlobalConfiguration config;

    @Override
    public Mono<Boolean> getVersion() {
        return this.makeCall(config, provider);
    }
}

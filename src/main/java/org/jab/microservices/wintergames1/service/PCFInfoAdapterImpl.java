package org.jab.microservices.wintergames1.service;

import lombok.AllArgsConstructor;
import org.jab.microservices.wintergames1.config.CloudFoundryProvider;
import org.jab.microservices.wintergames1.config.GlobalConfiguration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("pcf")
@AllArgsConstructor
public class PCFInfoAdapterImpl extends BaseCFInfoAdapter implements PCFInfoAdapter {

    private final CloudFoundryProvider provider = CloudFoundryProvider.PFC;
    private final GlobalConfiguration config;

    @Override
    public Mono<Boolean> getVersion() {
        return this.makeCall(config, provider);
    }

}

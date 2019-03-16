package org.jab.microservices.wintergames1.handler;

import org.jab.microservices.wintergames1.service.BluemixInfoAdapter;
import org.jab.microservices.wintergames1.service.PCFInfoAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MyHandlerTest {

    @MockBean(name = "pcf")
    private PCFInfoAdapter pcfInfoAdapter;

    @MockBean(name = "bluemix")
    private BluemixInfoAdapter bluemixInfoAdapter;

    @Autowired
    private MyHandler myHandler;

    @Disabled
    @Test
    public void Given_a_request_When_check_version_in_parallel_And_both_version_are_Ok_Then_returns_true() {

        when(pcfInfoAdapter.getVersion()).thenReturn(Mono.just(true));
        when(bluemixInfoAdapter.getVersion()).thenReturn(Mono.just(true));

        Mono<Boolean> result = myHandler.areVersionsOKParallel();

        StepVerifier.create(result)
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Disabled
    @Test
    public void Given_a_request_When_check_version_in_parallel_And_any_version_is_not_Ok_Then_returns_false() {

        when(pcfInfoAdapter.getVersion()).thenReturn(Mono.just(true));
        when(bluemixInfoAdapter.getVersion()).thenReturn(Mono.just(false));

        Mono<Boolean> result = myHandler.areVersionsOKParallel();

        StepVerifier.create(result)
                .expectNext(false)
                .expectComplete()
                .verify();
    }

    @Disabled
    @Test
    public void Given_a_request_When_check_version_in_sequence_And_both_version_are_Ok_Then_returns_true() {

        when(pcfInfoAdapter.getVersion()).thenReturn(Mono.just(true));
        when(bluemixInfoAdapter.getVersion()).thenReturn(Mono.just(true));

        Mono<Boolean> result = myHandler.areVersionsOKSequence();

        StepVerifier.create(result)
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Disabled
    @Test
    public void Given_a_request_When_check_version_in_sequence_And_any_version_is_not_Ok_Then_returns_false() {

        when(pcfInfoAdapter.getVersion()).thenReturn(Mono.just(true));
        when(bluemixInfoAdapter.getVersion()).thenReturn(Mono.just(false));

        Mono<Boolean> result = myHandler.areVersionsOKSequence();

        StepVerifier.create(result)
                .expectNext(false)
                .expectComplete()
                .verify();
    }
}
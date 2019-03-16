package org.jab.microservices.wintergames1.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CloudFoundryProvidersTest {

    @Test
    public void Given_the_pojo_When_the_option_is_PFC_Then_return_0() {

        assertTrue(CloudFoundryProvider.BLUEMIX.getIndex() == 1);
    }

    @Test
    public void Given_the_pojo_When_the_option_is_BLUEMIX_Then_return_1() {

        assertTrue(CloudFoundryProvider.BLUEMIX.getIndex() == 1);
    }

}
package org.jab.microservices.wintergames1.config;

import lombok.Data;

import java.util.List;

@Data
public class GlobalConfiguration {
    private List<Host> hosts;
}

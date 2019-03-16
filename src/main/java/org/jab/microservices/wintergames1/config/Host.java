package org.jab.microservices.wintergames1.config;

import lombok.Data;

@Data
public class Host {

    private CloudFoundryProvider id;
    private String  address;
    private Integer conntimeout;
    private String  version;
    private String  resource;
}

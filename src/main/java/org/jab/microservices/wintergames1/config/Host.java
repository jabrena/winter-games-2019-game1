package org.jab.microservices.wintergames1.config;

import lombok.Data;

@Data
public class Host {

    private CloudFoundryProviders id;
    private String  address;
    private Integer conntimeout;
    private Integer readtimeout;
    private Integer writetimeout;
    private String  version;
}

package org.jab.microservices.wintergames1.config;

import lombok.Data;

@Data
public class Host {

    private String  address;
    private Integer conntimeout;
    private Integer readtimeout;
    private Integer writetimeout;

}

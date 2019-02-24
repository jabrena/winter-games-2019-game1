package org.jab.microservices.wintergames1.config;

import lombok.*;

@Data
public class GlobalConfiguration {

    private String  pcf_host;
    private Integer pcf_conntimeout;
    private Integer pcf_readtimeout;
    private Integer pcf_writetimeout;

    //TODO Model better
    private String  bluemix_host;
    private Integer bluemix_conntimeout;
    private Integer bluemix_readtimeout;
    private Integer bluemix_writetimeout;
}

package org.jab.microservices.wintergames1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * This POJO is used to process the JSON from PCF & Bluemix
 */
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudFoundryInfoResponse {

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("api_version")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("api_version")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

}

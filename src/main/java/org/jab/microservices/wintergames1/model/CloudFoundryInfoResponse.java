package org.jab.microservices.wintergames1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloudFoundryInfoResponse that = (CloudFoundryInfoResponse) o;
        return Objects.equals(apiVersion, that.apiVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiVersion);
    }
}

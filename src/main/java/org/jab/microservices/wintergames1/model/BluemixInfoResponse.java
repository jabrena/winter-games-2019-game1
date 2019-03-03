package org.jab.microservices.wintergames1.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"name",
"build",
"support",
"version",
"description",
"authorization_endpoint",
"token_endpoint",
"min_cli_version",
"min_recommended_cli_version",
"api_version",
"app_ssh_endpoint",
"app_ssh_host_key_fingerprint",
"app_ssh_oauth_client",
"doppler_logging_endpoint",
"bits_endpoint"
})
public class BluemixInfoResponse {

    @JsonProperty("name")
    private String name;
    @JsonProperty("build")
    private String build;
    @JsonProperty("support")
    private String support;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("description")
    private String description;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    private String tokenEndpoint;
    @JsonProperty("min_cli_version")
    private Object minCliVersion;
    @JsonProperty("min_recommended_cli_version")
    private Object minRecommendedCliVersion;
    @JsonProperty("api_version")
    private String apiVersion;
    @JsonProperty("app_ssh_endpoint")
    private String appSshEndpoint;
    @JsonProperty("app_ssh_host_key_fingerprint")
    private String appSshHostKeyFingerprint;
    @JsonProperty("app_ssh_oauth_client")
    private String appSshOauthClient;
    @JsonProperty("doppler_logging_endpoint")
    private String dopplerLoggingEndpoint;
    @JsonProperty("bits_endpoint")
    private String bitsEndpoint;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("build")
    public String getBuild() {
        return build;
    }

    @JsonProperty("build")
    public void setBuild(String build) {
        this.build = build;
    }

    @JsonProperty("support")
    public String getSupport() {
        return support;
    }

    @JsonProperty("support")
    public void setSupport(String support) {
        this.support = support;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("authorization_endpoint")
    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    @JsonProperty("authorization_endpoint")
    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    @JsonProperty("token_endpoint")
    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    @JsonProperty("token_endpoint")
    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    @JsonProperty("min_cli_version")
    public Object getMinCliVersion() {
        return minCliVersion;
    }

    @JsonProperty("min_cli_version")
    public void setMinCliVersion(Object minCliVersion) {
        this.minCliVersion = minCliVersion;
    }

    @JsonProperty("min_recommended_cli_version")
    public Object getMinRecommendedCliVersion() {
        return minRecommendedCliVersion;
    }

    @JsonProperty("min_recommended_cli_version")
    public void setMinRecommendedCliVersion(Object minRecommendedCliVersion) {
        this.minRecommendedCliVersion = minRecommendedCliVersion;
    }

    @JsonProperty("api_version")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("api_version")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("app_ssh_endpoint")
    public String getAppSshEndpoint() {
        return appSshEndpoint;
    }

    @JsonProperty("app_ssh_endpoint")
    public void setAppSshEndpoint(String appSshEndpoint) {
        this.appSshEndpoint = appSshEndpoint;
    }

    @JsonProperty("app_ssh_host_key_fingerprint")
    public String getAppSshHostKeyFingerprint() {
        return appSshHostKeyFingerprint;
    }

    @JsonProperty("app_ssh_host_key_fingerprint")
    public void setAppSshHostKeyFingerprint(String appSshHostKeyFingerprint) {
        this.appSshHostKeyFingerprint = appSshHostKeyFingerprint;
    }

    @JsonProperty("app_ssh_oauth_client")
    public String getAppSshOauthClient() {
        return appSshOauthClient;
    }

    @JsonProperty("app_ssh_oauth_client")
    public void setAppSshOauthClient(String appSshOauthClient) {
        this.appSshOauthClient = appSshOauthClient;
    }

    @JsonProperty("doppler_logging_endpoint")
    public String getDopplerLoggingEndpoint() {
        return dopplerLoggingEndpoint;
    }

    @JsonProperty("doppler_logging_endpoint")
    public void setDopplerLoggingEndpoint(String dopplerLoggingEndpoint) {
        this.dopplerLoggingEndpoint = dopplerLoggingEndpoint;
    }

    @JsonProperty("bits_endpoint")
    public String getBitsEndpoint() {
        return bitsEndpoint;
    }

    @JsonProperty("bits_endpoint")
    public void setBitsEndpoint(String bitsEndpoint) {
        this.bitsEndpoint = bitsEndpoint;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("build", build).append("support", support).append("version", version).append("description", description).append("authorizationEndpoint", authorizationEndpoint).append("tokenEndpoint", tokenEndpoint).append("minCliVersion", minCliVersion).append("minRecommendedCliVersion", minRecommendedCliVersion).append("apiVersion", apiVersion).append("appSshEndpoint", appSshEndpoint).append("appSshHostKeyFingerprint", appSshHostKeyFingerprint).append("appSshOauthClient", appSshOauthClient).append("dopplerLoggingEndpoint", dopplerLoggingEndpoint).append("bitsEndpoint", bitsEndpoint).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(appSshHostKeyFingerprint)
                .append(apiVersion)
                .append(dopplerLoggingEndpoint)
                .append(appSshEndpoint)
                .append(support)
                .append(bitsEndpoint)
                .append(build)
                .append(appSshOauthClient)
                .append(version)
                .append(minCliVersion)
                .append(additionalProperties)
                .append(description)
                .append(minRecommendedCliVersion)
                .append(name)
                .append(tokenEndpoint)
                .append(authorizationEndpoint)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BluemixInfoResponse) == false) {
            return false;
        }

        BluemixInfoResponse rhs = ((BluemixInfoResponse) other);
        return new EqualsBuilder()
                .append(appSshHostKeyFingerprint, rhs.appSshHostKeyFingerprint)
                .append(apiVersion, rhs.apiVersion)
                .append(dopplerLoggingEndpoint, rhs.dopplerLoggingEndpoint)
                .append(appSshEndpoint, rhs.appSshEndpoint)
                .append(support, rhs.support)
                .append(bitsEndpoint, rhs.bitsEndpoint)
                .append(build, rhs.build)
                .append(appSshOauthClient, rhs.appSshOauthClient)
                .append(version, rhs.version)
                .append(minCliVersion, rhs.minCliVersion)
                .append(additionalProperties, rhs.additionalProperties)
                .append(description, rhs.description)
                .append(minRecommendedCliVersion, rhs.minRecommendedCliVersion)
                .append(name, rhs.name)
                .append(tokenEndpoint, rhs.tokenEndpoint)
                .append(authorizationEndpoint, rhs.authorizationEndpoint)
                .isEquals();
    }

}
package org.jab.microservices.wintergames1.model;

import com.fasterxml.jackson.annotation.*;
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
"app_ssh_endpoint",
"app_ssh_host_key_fingerprint",
"app_ssh_oauth_client",
"doppler_logging_endpoint",
"api_version",
"osbapi_version",
"routing_endpoint"
})
public class InfoResponse {

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
    private String minCliVersion;
    @JsonProperty("min_recommended_cli_version")
    private String minRecommendedCliVersion;
    @JsonProperty("app_ssh_endpoint")
    private String appSshEndpoint;
    @JsonProperty("app_ssh_host_key_fingerprint")
    private String appSshHostKeyFingerprint;
    @JsonProperty("app_ssh_oauth_client")
    private String appSshOauthClient;
    @JsonProperty("doppler_logging_endpoint")
    private String dopplerLoggingEndpoint;
    @JsonProperty("api_version")
    private String apiVersion;
    @JsonProperty("osbapi_version")
    private String osbapiVersion;
    @JsonProperty("routing_endpoint")
    private String routingEndpoint;
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
    public String getMinCliVersion() {
    return minCliVersion;
    }

    @JsonProperty("min_cli_version")
    public void setMinCliVersion(String minCliVersion) {
    this.minCliVersion = minCliVersion;
    }

    @JsonProperty("min_recommended_cli_version")
    public String getMinRecommendedCliVersion() {
    return minRecommendedCliVersion;
    }

    @JsonProperty("min_recommended_cli_version")
    public void setMinRecommendedCliVersion(String minRecommendedCliVersion) {
    this.minRecommendedCliVersion = minRecommendedCliVersion;
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

    @JsonProperty("api_version")
    public String getApiVersion() {
    return apiVersion;
    }

    @JsonProperty("api_version")
    public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
    }

    @JsonProperty("osbapi_version")
    public String getOsbapiVersion() {
    return osbapiVersion;
    }

    @JsonProperty("osbapi_version")
    public void setOsbapiVersion(String osbapiVersion) {
    this.osbapiVersion = osbapiVersion;
    }

    @JsonProperty("routing_endpoint")
    public String getRoutingEndpoint() {
    return routingEndpoint;
    }

    @JsonProperty("routing_endpoint")
    public void setRoutingEndpoint(String routingEndpoint) {
    this.routingEndpoint = routingEndpoint;
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
    return new ToStringBuilder(this).append("name", name).append("build", build).append("support", support).append("version", version).append("description", description).append("authorizationEndpoint", authorizationEndpoint).append("tokenEndpoint", tokenEndpoint).append("minCliVersion", minCliVersion).append("minRecommendedCliVersion", minRecommendedCliVersion).append("appSshEndpoint", appSshEndpoint).append("appSshHostKeyFingerprint", appSshHostKeyFingerprint).append("appSshOauthClient", appSshOauthClient).append("dopplerLoggingEndpoint", dopplerLoggingEndpoint).append("apiVersion", apiVersion).append("osbapiVersion", osbapiVersion).append("routingEndpoint", routingEndpoint).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
    return new HashCodeBuilder().append(apiVersion).append(appSshHostKeyFingerprint).append(dopplerLoggingEndpoint).append(appSshEndpoint).append(support).append(build).append(appSshOauthClient).append(version).append(minCliVersion).append(routingEndpoint).append(additionalProperties).append(description).append(osbapiVersion).append(minRecommendedCliVersion).append(name).append(tokenEndpoint).append(authorizationEndpoint).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
    if (other == this) {
    return true;
    }
    if ((other instanceof InfoResponse) == false) {
    return false;
    }
    InfoResponse rhs = ((InfoResponse) other);
    return new EqualsBuilder().append(apiVersion, rhs.apiVersion).append(appSshHostKeyFingerprint, rhs.appSshHostKeyFingerprint).append(dopplerLoggingEndpoint, rhs.dopplerLoggingEndpoint).append(appSshEndpoint, rhs.appSshEndpoint).append(support, rhs.support).append(build, rhs.build).append(appSshOauthClient, rhs.appSshOauthClient).append(version, rhs.version).append(minCliVersion, rhs.minCliVersion).append(routingEndpoint, rhs.routingEndpoint).append(additionalProperties, rhs.additionalProperties).append(description, rhs.description).append(osbapiVersion, rhs.osbapiVersion).append(minRecommendedCliVersion, rhs.minRecommendedCliVersion).append(name, rhs.name).append(tokenEndpoint, rhs.tokenEndpoint).append(authorizationEndpoint, rhs.authorizationEndpoint).isEquals();
    }

}
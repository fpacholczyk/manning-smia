package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    // Thanks to @EnableDiscoveryClient in LicenseServiceApplication, RestTemplate have Discovery Client injected.
    @Autowired
    RestTemplate restTemplate;

    @CircuitBreaker(name = "organizationService")
    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        // Instead of URL, use Eureka service application id.
                        // Appropriate instance ID will be substituted automatically.
                        "http://organization-service/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}

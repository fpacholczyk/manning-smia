package com.optimagrowth.organization.controller;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.service.OrganizationService;
import com.optimagrowth.organization.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organization")
public class OrganizationController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService service;

    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
        logger.debug("OrganizationController#getOrganization Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId());
        return ResponseEntity.of(service.findById(organizationId));
    }

    @PutMapping(value = "/{organizationId}")
    public void updateOrganization(@PathVariable("organizationId") String id, @RequestBody Organization organization) {
        service.update(organization);
    }

    @PostMapping
    public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(service.create(organization));
    }

    @DeleteMapping(value = "/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("organizationId") String organizationId) {
        service.delete(organizationId);
    }

}

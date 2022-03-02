package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value="v1/organization/{organizationId}/license")
public class LicenseController {

    private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);

    @Autowired
    private LicenseService licenseService;

    @RequestMapping(value="/{licenseId}", method = RequestMethod.GET)
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {

        License license = licenseService.getLicense(licenseId, organizationId, locale);
        license.add(
            linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId(), locale)).withSelfRel(),
            linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
            linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
            linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId(), locale)).withRel("deleteLicense")
        );

        return ResponseEntity.ok(license);
    }

    @RequestMapping(value="/{licenseId}/{clientType}", method = RequestMethod.GET)
    public License getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @PathVariable("clientType") String clientType,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return licenseService.getLicense(licenseId, organizationId, clientType.toLowerCase(), locale);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.createLicense(request));
    }

    @DeleteMapping(value="/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, locale));
    }

    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) throws TimeoutException {
        logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrganization(organizationId);
    }
}

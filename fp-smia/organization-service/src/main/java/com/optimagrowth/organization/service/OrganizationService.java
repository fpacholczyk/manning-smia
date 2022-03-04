package com.optimagrowth.organization.service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    public Optional<Organization> findById(String organizationId) {
        return repository.findById(organizationId);
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        return organization;
    }

    public void update(Organization organization) {
        repository.save(organization);
    }

    public void delete(String organizationId) {
        repository.deleteById(organizationId);
    }
}
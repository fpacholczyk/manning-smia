package com.optimagrowth.organization.events.service;

import com.optimagrowth.organization.events.model.Action;
import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    @Autowired
    private StreamBridge streamBridge;

    @Value("${spring.cloud.stream.source}")
    private String streamSource;

    public void publishOrganizationChange(Action action, String organizationId) {
        OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action.toString(),
                organizationId,
                UserContextHolder.getContext().getCorrelationId()
        );

        logger.debug("Sending Kafka message {} for organization id: {}. Correlation ID: {}",
                action, organizationId, change.getCorrelationId());

        streamBridge.send(streamSource + "-out-0", change);
    }
}

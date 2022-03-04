package com.optimagrowth.organization.events.service;

import com.optimagrowth.organization.events.model.Action;
import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    private final Source source;

    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishOrganizationChange(Action action, String organizationId) {
        OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action.toString(),
                organizationId,
                UserContextHolder.getContext().getCorrelationId()
        );

        logger.debug("Sending Kafka message {} for organization id: {}. Correlation ID: {}",
                action, organizationId, change.getCorrelationId());

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}

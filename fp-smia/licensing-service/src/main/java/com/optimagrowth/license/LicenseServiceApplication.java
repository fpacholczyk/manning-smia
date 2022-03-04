package com.optimagrowth.license;

import com.optimagrowth.license.events.model.OrganizationChangeModel;
import com.optimagrowth.license.utils.UserContextInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(Sink.class)
public class LicenseServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(LicenseServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LicenseServiceApplication.class, args);
    }

    // AcceptHeaderLocaleResolver is already created in WebMvcAutoConfiguration.EnableWebMvcConfiguration#localeResolver,
    //
    // @Bean
    // public LocaleResolver localeResolver() {
    // 	SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    // 	localeResolver.setDefaultLocale(Locale.US);
    // 	return localeResolver;
    // }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasenames("messages");
        return messageSource;
    }

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        // Note: thanks to @EnableDiscoveryClient, every RestTemplate will now have Discovery Client injected.
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
        interceptors.add(new UserContextInterceptor());
        restTemplate.setInterceptors(List.copyOf(interceptors));
        return restTemplate;
    }

    @StreamListener(Sink.INPUT)
    public void loggerSink(OrganizationChangeModel change) {
        logger.debug("Received an {} event for organization id: {}. Correlation ID: {}",
                change.getAction(), change.getOrganizationId(), change.getCorrelationId());
    }
}

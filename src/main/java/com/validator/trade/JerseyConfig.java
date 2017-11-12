package com.validator.trade;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Defines the ReST Resource Configurations.
 */
@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(TradeValidatorResource.class);
    }
}
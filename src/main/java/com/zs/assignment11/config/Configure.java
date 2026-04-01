package com.zs.assignment11.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Configure.
 */
@Configuration
@EnableCaching
@EnableTransactionManagement
public class Configure {
    /**
     * Observed aspect.
     *
     * @param observationRegistry the observation registry
     * @return the observed aspect
     */
    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}

package com.zs.assignment11.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Enable transaction.
 */
@Configuration
@EnableCaching
@EnableTransactionManagement
public class Configure {
}

package com.compdes.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Getter
@Setter
@Configuration

public class AppProperties {

    @Value("${app.frontendHost}")
    private String frontendHost;

    @Value("${backend.host}")
    private String backendHost;

    @Value("${spring.mail.username}")
    private String mailFrom;
}

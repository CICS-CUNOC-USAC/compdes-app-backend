package com.compdes.common.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuración global de zona horaria para la aplicación.
 * 
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-19
 */
@Configuration
public class TimeZoneConfig {

    /**
     * Establece la zona horaria por defecto en "America/Guatemala".
     * 
     * Este método se ejecuta automáticamente después de la construcción del
     * contexto de Spring y garantiza que cualquier operación con fechas utilice
     * la zona horaria especificada.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guatemala"));
    }
}

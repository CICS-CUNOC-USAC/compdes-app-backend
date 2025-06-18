package com.compdes.common.config.web;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * Configuración de Swagger/OpenAPI para la documentación automática de la API
 * REST.
 *
 * Esta clase define la configuración necesaria para exponer la documentación de
 * la API utilizando Swagger UI a través de la especificación OpenAPI 3.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("API App Compdes")
                        .version("1.0")
                        .description("Documentación de la API para el proyecto de Compdes"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public") // grupo de endpoints públicos
                .pathsToMatch("/api/**") // se define el patrón de las rutas a documentar
                .build();
    }
}

package com.compdes.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final AppProperties appProperties;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activa CORS
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/v1/login").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/participants").permitAll()
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                                // vamos a resguardar las rutas con los permisos necesarios
                                                .anyRequest().authenticated() // Protege el resto de rutas
                                )
                                // sin sesiones
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.getOrBuild();
        }

        /**
         * Configuración de CORS personalizada
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                // agrega todas las rutas permitidas
                configuration.setAllowedOrigins(List.of(appProperties.getFrontendHost()));

                // decimos que operaciones http estan permitidos
                configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));

                // decimos que headers estan permitidos
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                // aplicamos CORS a todas las rutas del sistema
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        /**
         * Configura el bean que sera expueto cuando se necesite el cripter en el
         * sistema, se eligio esta implementacion porque utiliza BCrypt (version 2B para
         * compatibilidad con
         * caracteres especiales) y 12 iteraciones en el algoritmo.
         *
         * @return
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(BCryptVersion.$2B, 12);
        }

}

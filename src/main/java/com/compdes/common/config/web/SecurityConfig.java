package com.compdes.common.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.compdes.auth.jwt.filter.JwtAuthenticationFilter;
import com.compdes.common.config.AppProperties;
import com.compdes.common.enums.PublicEndpointsEnum;

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

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activa CORS
                                .authorizeHttpRequests(auth -> {
                                        for (PublicEndpointsEnum route : PublicEndpointsEnum.values()) {
                                                if (route.getMethod() == null) {
                                                        auth.requestMatchers(route.getPath()).permitAll();
                                                } else {
                                                        auth.requestMatchers(route.getMethod(), route.getPath())
                                                                        .permitAll();
                                                }
                                        }
                                        auth.anyRequest().authenticated(); // Resto de rutas protegidas
                                })
                                // sin sesiones
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
                configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));

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

package com.compdes.auth.jwt.services;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.compdes.auth.jwt.models.JwtConfig;
import com.compdes.auth.users.models.entities.CompdesUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la generación de tokens JWT para usuarios autenticados.
 * 
 * Este servicio construye tokens firmados con información básica del usuario
 * como el rol y el nombre, utilizando una clave secreta definida en la
 * configuración.
 * 
 * El token generado incluye fecha de emisión, expiración y está firmado con
 * HMAC-SHA256.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Service
@RequiredArgsConstructor
public class JwtGeneratorService {

    private final JwtConfig jwtConfig;

    /**
     * Nombres de las claims que seran incluidas en el toke JWT
     */
    public static final String CLAIM_NAME_USER_TYPE = "userType";

    /**
     * Cnfiguracion del timpo de valides del token JWT
     */
    private static final Long JWT_TOKEN_VALIDITY_HOURS = 48L;
    private static final Long JWT_TOKEN_TIME_VALIDITY = Duration.ofHours(JWT_TOKEN_VALIDITY_HOURS).toMillis();

    private static final String ROLE_KEY = "ROLE_";

    /**
     * Genera un token JWT para el usuario autenticado.
     * 
     * Este método construye un conjunto básico de claims, incluyendo el rol actual
     * del usuario como autoridad, y luego delega la creación del token firmado.
     * 
     * @param compdesUser el usuario autenticado para quien se generará el token
     * @return un JWT como cadena codificada, que contiene la identidad y rol del
     *         usuario
     */
    public String generateToken(CompdesUser compdesUser) {
        // mandmaos a cargar las clims (las base porue solo esas son necesarias)
        Map<String, Object> claims = new HashMap<>();

        // Agregar el rol del usuario en las autorities
        claims.put(CLAIM_NAME_USER_TYPE, ROLE_KEY + compdesUser.getRole().name());

        // Generar el token
        return createToken(claims, compdesUser.getUsername());
    }

    /**
     * Crea un token JWT firmado a partir de las claims y el nombre de usuario.
     * 
     * El token generado incluye una fecha de emisión, fecha de expiración
     * y se firma utilizando la clave secreta configurada.
     *
     * @param claims   Claims a incluir en el token.
     * @param username Nombre de usuario autenticado.
     * @return Token JWT firmado como cadena.
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_TIME_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}

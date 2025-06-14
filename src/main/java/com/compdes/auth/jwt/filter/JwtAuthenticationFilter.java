package com.compdes.auth.jwt.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.compdes.auth.jwt.utils.JwtTokenInspector;
import com.compdes.common.enums.PublicEndpointsEnum;
import com.compdes.common.exceptions.InvalidTokenException;
import com.compdes.common.models.dto.response.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-27
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenInspector jwtTokenInspector;

    /**
     * Método principal que intercepta cada solicitud HTTP y ejecuta la
     * validación del token JWT.
     *
     * @param request     La solicitud HTTP entrante.
     * @param response    La respuesta HTTP saliente.
     * @param filterChain La cadena de filtros de seguridad.
     * @throws ServletException Si ocurre un error de filtro.
     * @throws IOException      Si ocurre un error de E/S.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Saltar validación si es endpoint público
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> tokenOptional = extractTokenFromHeader(request);

        if (tokenOptional.isPresent()) {

            try {
                String token = tokenOptional.get();
                Optional<UserDetails> userDetailsOptional = validateToken(token);
                if (userDetailsOptional.isPresent()) {
                    authenticateUser(userDetailsOptional.get(), token, request);
                }

            } catch (InvalidTokenException ex) {
                log.warn(ex.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(
                        new ErrorDTO(
                                "Token inválido o expirado. Código: " + ex.getCode())));
            }

        }
        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si la URI corresponde a un endpoint público permitido sin
     * autenticación.
     *
     * @param path URI de la solicitud.
     * @return true si el path es público, false si requiere autenticación.
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();

        for (PublicEndpointsEnum endpointsEnum : PublicEndpointsEnum.values()) {
            if (matcher.match(endpointsEnum.getPath(), requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extrae el token JWT del encabezado `Authorization` de la solicitud.
     *
     * @param request La solicitud HTTP de la que se extraerá el token.
     * @return Un {@code Optional} que contiene el token JWT si está presente y
     *         es válido.
     */
    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }

    /**
     * Valida el token JWT y devuelve los detalles del usuario si el token es
     * válido.
     *
     * @param jwt El token JWT a validar.
     * @return Un {@code Optional} con los detalles del usuario si el token es
     *         válido.
     * @throws InvalidTokenException
     */
    private Optional<UserDetails> validateToken(String jwt) {

        String username = jwtTokenInspector.extractUsername(jwt);
        String userType = jwtTokenInspector.extractUserType(jwt);

        // Validar si el token ya ha sido autenticado
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return Optional.empty();
        }

        // creamos el usuario Spring para que sea cargado en el contexto
        User user = new User(username, "", List.of(new SimpleGrantedAuthority(userType)));

        if (jwtTokenInspector.isTokenValid(jwt)) {
            log.info("Usuario autenticado exitosamente: {}", username);
            return Optional.of(user);
        }

        log.warn("Token JWT inválido para el usuario: {}", username);
        return Optional.empty();

    }

    /**
     * Autentica al usuario y establece el contexto de seguridad de Spring.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param token       El token JWT usado para la autenticación.
     * @param request     La solicitud HTTP actual.
     */
    private void authenticateUser(UserDetails userDetails, String token, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

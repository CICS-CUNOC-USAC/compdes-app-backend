package com.compdes.auth.jwt.models;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de parámetros relacionados con JWT.
 * 
 * Esta clase proporciona el acceso a la clave secreta usada para la
 * codificación y decodificación de tokens JWT, leyendo el valor desde
 * el archivo de propiedades de configuración.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Devuelve la clave secreta como un arreglo de bytes, decodificándola desde
     * Base64.
     * 
     * @return un arreglo de bytes correspondiente a la clave secreta decodificada.
     */
    public byte[] getSecretBytes() {
        return Base64.getDecoder().decode(secretKey);
    }
}

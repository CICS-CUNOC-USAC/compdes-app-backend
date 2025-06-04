package com.compdes.auth.login.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.jwt.services.JwtGeneratorService;
import com.compdes.auth.login.models.dto.request.LoginDTO;
import com.compdes.auth.login.models.dto.response.LoginResponseDTO;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.services.CompdesUserService;
import com.compdes.common.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar el proceso de autenticación de usuarios.
 * Verifica las credenciales del usuario y, si son válidas, genera un token JWT.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoginService {

    private final CompdesUserService compdesUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGeneratorService jwtGeneratorService;

    /**
     * Autentica a un usuario en el sistema utilizando su correo o nombre de usuario
     * y contraseña.
     * Si las credenciales son válidas, genera y retorna un token JWT junto con
     * información del usuario.
     *
     * @param loginDTO objeto que contiene el nombre de usuario y la contraseña
     * @return LoginResponseDTO con el nombre de usuario, el rol y el token JWT
     * @throws NotFoundException       si el usuario no se encuentra en el sistema
     * @throws BadCredentialsException si la contraseña es incorrecta o el usuario
     *                                 no existe
     */
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            // mandar a buscar el usuario por user name
            CompdesUser user = compdesUserService.findUserByUsername(loginDTO.getUsername());

            // vemos si las contrasenas coinciden
            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("");
            }

            // si no fallo enntonces generar el token y retornar la respuesta
            String token = jwtGeneratorService.generateToken(user);
            return new LoginResponseDTO(user.getUsername(), user.getRole().name(), token);

        } catch (NotFoundException e) {
            throw new BadCredentialsException("");
        }

    }
}

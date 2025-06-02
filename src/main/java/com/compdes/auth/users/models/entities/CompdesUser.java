package com.compdes.auth.users.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.common.models.entities.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un usuario dentro del sistema Compdes.
 * Incluye autenticación básica por username y password, y asignación de roles.
 * 
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CompdesUser extends Auditor {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolesEnum role;

    public CompdesUser(String username, String password, RolesEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}

package com.victor.project.gymapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * Representa un usuario en forma de DTO
 * Aun por mejorar e implementar
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Size(min = 6, max = 30, message = "El nombre de usuario debe contener al menos 6 caracteres y un máximo de 30")
    private String username;// Nombre de usuario

    @Size(min = 6, max = 50, message = "El email debe contener al menos 6 caracteres y un máximo de 30")
    @Email
    private String email;// email del usuario

    @Size(min = 6, max = 30, message = "La contraseña debe tener al menos 6 carateres y máximo 30")
    private String password;

}

package com.victor.project.gymapp.dto;

import com.victor.project.gymapp.services.validation.ExistUserEmailDb;
import com.victor.project.gymapp.services.validation.ExistUserNameDb;
import com.victor.project.gymapp.services.validation.PasswordMatches;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * Representa un usuario en forma de DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches //Comprobará que ambas contraseñas coincidan
public class UserDto {

    @NotBlank(message = "Campo usuario no debe estar vacio")
    @Size(min = 6, max = 30, message = "El nombre de usuario debe contener al menos 6 caracteres y un máximo de 30")
    @ExistUserNameDb
    private String username;// Nombre de usuario

    @NotBlank(message = "Campo email no debe estar vacio")
    @Size(min = 6, max = 50, message = "El email debe contener al menos 6 caracteres y un máximo de 30")
    @Email(message = "Email incorrecto")
    @ExistUserEmailDb
    private String email;// email del usuario

    @NotBlank(message = "Campo contraseña no debe estar vacio")
    @Size(min = 6, max = 30, message = "La contraseña debe tener al menos 6 carateres y máximo 30")
    private String password; //Contraseña del usuario

    @NotBlank(message = "Campo contraseña no debe estar vacio")
    @Size(min = 6, max = 30, message = "La contraseña debe tener al menos 6 carateres y máximo 30")
    private String matchingPassword; //La misma contraseña, deben coincidir

}

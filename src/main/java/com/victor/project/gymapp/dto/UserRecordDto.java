package com.victor.project.gymapp.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * Representa un registro de usuario en forma de DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRecordDto {

    //Id del registro
    private Integer id;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;// Fecha en la que se hace el registro

    @NotNull(message = "La altura es obligatoria")
    @Max(value = 250, message = "La altura debe ser menor o igual a 250")
    @Min(value = 0, message = "La altura debe ser mayor que 0")
    private Short height;// Altura del usuario en cm

    @NotNull(message = "El peso es obligatorio")
    @Max(value = 500, message = "El peso debe ser menor o igual a 500")
    @Min(value = 0, message = "El peso debe ser mayor que 0")
    private Float weight;// Peso del usuario en kg

    @Size(max=255, message = "El comentario no debe contener más de 255 caracteres")
    private String userRecordComment;//comentario asociado al registro


}

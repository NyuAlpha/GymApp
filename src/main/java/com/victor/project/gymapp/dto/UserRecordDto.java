package com.victor.project.gymapp.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRecordDto {

    private Integer id;

    @NotNull(message = "Campo fecha obligatorio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;// Fecha en la que se hace el registro

    @Max(value = 250, message = "Debe ser menor o igual a 250")
    @Min(value = 0, message = "Debe ser mayor que 0")
    private Short height;// Altura del usuario en cm

    @Max(value = 500, message = "Debe ser menor o igual a 500")
    @Min(value = 0, message = "Debe ser mayor que 0")
    private Float weight;// Peso del usuario en kg
}

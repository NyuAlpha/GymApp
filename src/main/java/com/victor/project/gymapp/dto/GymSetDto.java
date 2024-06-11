package com.victor.project.gymapp.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un dto de un set de ejercicios en el gimnasio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GymSetDto {


    private Integer exerciseId; // ID del ejercicio al que pertenece este set, parte de la clave primaria
    private Byte setOrder; // Orden del set en la lista de series, parte de la clave primaria

    @Min(value = 0, message = "Debe ser mayor o igual a 0")
    @Max(value = 999, message = "Debe ser menor que 1000")
    private BigDecimal weight;// El peso a levantar, puede ser nulo en caso

    @Min(value = 0, message = "Debe ser mayor o igual a 0")
    @Max(value = 127, message = "Debe ser menor que 127")
    private Integer repetitions;// Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    private Boolean failure;

    @Min(value = 0, message = "Debe ser mayor o igual a 0")
    @Max(value = 127, message = "Debe ser menor que 127")
    private Integer timesRepeated;


    //Constructor básico con campo obligatorio
    public GymSetDto(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }


}

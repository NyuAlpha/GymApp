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

    @Min(value = 0, message = "El peso debe ser mayor o igual a 0")
    @Max(value = 999, message = "El peso debe ser menor que 1000")
    private BigDecimal weight;// El peso a levantar, puede ser nulo en caso

    @Min(value = 0, message = "Las repeticiones deben ser mayores o iguales a 0")
    @Max(value = 127, message = "Las repeticiones deben ser menores que 128")
    private Integer repetitions;// Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    @NotNull(message = "Fallo debe ser verdadero o falso")
    private Boolean failure=false;

    @Min(value = 1, message = "El número de series debe ser mayor o igual a 1")
    @Max(value = 127, message = "El número de series debe ser menor que 128")
    private Integer timesRepeated;





    

    //Constructor básico para asignarle entrenamiento padre
    public GymSetDto(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }


}

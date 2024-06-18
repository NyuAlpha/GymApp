package com.victor.project.gymapp.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * Representa una temporada en forma de DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeasonDto {

    //Id de la temporada
    private Integer id;

    @NotBlank(message = "El título no debe estar vacío")
    @Size(min = 5, max = 50, message = "El título debe contener entre 5 y 30 caracteres")
    private String title;//Título de la temporada

    @NotNull(message = "La fecha de inicio no debe estar vacía")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;//Fecha de comienzo

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;//Fecha de finalización

    @Size(max = 255, message = "El comentario no debe de tener más de 255 caracteres")
    private String seasonComment;//comentario asignado


}

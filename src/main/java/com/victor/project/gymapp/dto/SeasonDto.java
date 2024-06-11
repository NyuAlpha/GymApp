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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeasonDto {

    private Integer id;

    @NotBlank(message = "El título no debe estar vacío")
    @Size(min = 5, max = 50, message = "Debe contener entre 5 y 30 caracteres")
    private String title;

    @NotNull(message = "La fecha de inicio no debe estar vacía")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Size(max = 100, message = "No debe de tener más de 100 caracteres")
    private String seasonComment;

    // private String idUser;//Es un uuid del usuario

    // private Set<TrainingDto> trainingsDto;

}

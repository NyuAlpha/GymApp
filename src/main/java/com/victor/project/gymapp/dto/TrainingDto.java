package com.victor.project.gymapp.dto;

import java.time.LocalDate;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Representa un entrenamiento de la BBDD en forma de DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class TrainingDto {

    private Integer id; //Id del ejercicio
    private Integer seasonId; //Id de la temporada a la que pertenece

    @NotNull(message = "La fecha no debe estar vacía")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;//Fecha en la que se realiza el entrenamiento

    @Size(max=30, message = "El título no debe contener más de 30 caracteres")
    private String title;//Titulo del entrenamiento

    @Size(max=255, message = "El comentario no debe contener más de 255 caracteres")
    private String trainingComment;// comentario asociado al entrenamiento

    // Los ejercicios asociados
    private Set<ExerciseDto> exerciseDtos; 





    //Crea un TrainingDto con una fecha y un comentario
    public TrainingDto(LocalDate date, String trainingComment) {
        this.date = date;
        this.trainingComment = trainingComment;
    }

    //Crea un TrainingDto con una fecha
    public TrainingDto(LocalDate date) {
        this.date = date;
    }

}

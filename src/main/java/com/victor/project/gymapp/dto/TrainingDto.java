package com.victor.project.gymapp.dto;

import java.time.LocalDate;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingDto {

    private Integer id;
    private Integer seasonId; //Temporada a la que pertenece

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;

    @Size(max=30, message = "No debe contener más de 30 caracteres")
    private String title;

    @Size(max=100, message = "No debe contener más de 100 caracteres")
    private String trainingComment;// comentario asociado al entrenamiento, debe cargar siempre

    private Set<ExerciseDto> exerciseDtos; // Los ejercicios asociados

    public TrainingDto(LocalDate date, String trainingComment) {
        this.date = date;
        this.trainingComment = trainingComment;
    }

    public TrainingDto(LocalDate date) {
        this.date = date;
    }

}

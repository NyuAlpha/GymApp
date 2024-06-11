package com.victor.project.gymapp.models;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.dto.TrainingDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
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
@Entity
@Table(name="trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 30)
    private String title;
    
    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL, orphanRemoval = true)
    private TrainingComment trainingComment;//comentario asociado al entrenamiento
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "training" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercise> exercises = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_season")
    private Season season;

    /*
     * Constructor para convertir de Dto a Entity
     */
    public Training(TrainingDto trainingDto){
        update(trainingDto);
    }

    /*
     * Este método sirve para obtener todos los datos que necesita la vista del entrenamiento
     */
    public TrainingDto getFullDto() {


        TrainingDto trainingDto = getSimpleDto();

        if(exercises != null){
            Set<ExerciseDto> exerciseDtos = exercises.stream()
                .map(exercise -> exercise.getDto())
                .sorted(Comparator.comparing(ExerciseDto::getExerciseOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new));

            trainingDto.setExerciseDtos(exerciseDtos);
        }
        return trainingDto;
    }



    // Obtiene los campos elementales para mostrar el entrenamiento en forma de lista
    public TrainingDto getSimpleDto() {

        TrainingDto trainingDto = new TrainingDto();

        // Parámetros obligatorios
        trainingDto.setId(id);
        trainingDto.setDate(date);

        // Parametros no obligatorios
        trainingDto.setTitle(title);

        //Ahora carga el id de su temporada asignada
        trainingDto.setSeasonId(season.getId());

        if (trainingComment != null) // Si tiene un comentario se asigna
        trainingDto.setTrainingComment(trainingComment.getComment());

        return trainingDto;
    }

    //Actualiza los campos en base al Dto
    public void update(TrainingDto trainingDto){

        id = trainingDto.getId();
        title = trainingDto.getTitle();
        date = trainingDto.getDate();

        if(trainingDto.getTrainingComment() != null || !trainingDto.getTrainingComment().isBlank()){
            trainingComment = new TrainingComment(null, trainingDto.getTrainingComment());
        }
    }
    
}

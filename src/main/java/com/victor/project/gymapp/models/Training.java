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

/*
 * Esta clase representa un entrenamiento de la BBDD, además de una tabla dentro de esta.
 */
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
    private Integer id;//Primary key

    @Column(nullable = false)
    private LocalDate date;//Fecha en la que se realiza el entrenamiento

    @Column(length = 30)
    private String title;//Título o nombre que tendrá el entrenamiento, auqnue no es obligatorio
    
    @Column(length = 255, name = "training_comment")
    private String trainingComment;//comentario asociado al entrenamiento, no es obligatorio
    

    //Ejercicios que contiene el entrenamiento, no es necesario que carguen por defecto
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "training" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercise> exercises = new HashSet<>();

    //Temporada a la que pertenece este entrenamiento, siempre debe cargar
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_season")
    private Season season;









    /*
     * Constructor para convertir de Dto a Entity
     */
    public Training(TrainingDto trainingDto){
        id = null; //El id siempre es null cuando se crea un Training
        update(trainingDto);
    }






    /*
     * Este método sirve para obtener todos los datos que necesita la vista del entrenamiento
     */
    public TrainingDto getFullDto() {

        //Obtenemos el dtp con los datos simples
        TrainingDto trainingDto = getSimpleDto();

        //si existen ejercicios itera a través de ellos y los pasa a dto todos
        if(exercises != null){
            Set<ExerciseDto> exerciseDtos = exercises.stream()
                .map(exercise -> exercise.getDto())
                .sorted(Comparator.comparing(ExerciseDto::getExerciseOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new));
            //Los guarda en el exercise DTO
            trainingDto.setExerciseDtos(exerciseDtos);
        }
        return trainingDto;
    }








    /*
     * Obtiene los campos en forma de dto para mostrar el entrenamiento en forma de lista en la temporada padre
     */
    public TrainingDto getSimpleDto() {

        TrainingDto trainingDto = new TrainingDto();

        // Parámetros obligatorios
        trainingDto.setId(id);
        //Ahora carga el id de su temporada asignada
        trainingDto.setSeasonId(season.getId());

        trainingDto.setDate(date);
        trainingDto.setTitle(title);
        trainingDto.setTrainingComment(trainingComment);

        return trainingDto;
    }








    /*
     * Actualiza los campos de esta entidad en base a los datos del Dto, la Season se establece a parte
     */
    public void update(TrainingDto trainingDto){

        //Obligatorio, no se comrpueba
        date = trainingDto.getDate();

        //Si viene vacios serán null
        String t = trainingDto.getTitle();
        title = (t.isBlank())?  null : t;

        String c = trainingDto.getTrainingComment();
        trainingComment = (c.isBlank())?  null : c;

    }
    
}

package com.victor.project.gymapp.models;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.dto.GymSetDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un ejercicio , con su nombre, y algunos datos importantes.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//Primary key

    //Orden del ejercicio en el contexto de un entrenamiento, es obligatorio, pero se gestiona internamente
    @Column(name = "exercise_order", nullable = false)
    private Byte exerciseOrder;

    @ManyToOne()//Un ejercicio debe tener un solo nombre, pero los nombres pueden estar en muchos ejercicios, obligatorio
    @JoinColumn(name="exercise_name_id", nullable = false)
    private ExerciseName exerciseName;

    @Column(length = 30)
    private String variant;//Un ejercicio puede tener variaciones o elementos adicionales, no obligatorio

    @Column(length = 255, name="exercise_comment")
    private String exerciseComment;//comentario asociado a un ejercicio, no es obligatorio

    @ManyToOne()
    @JoinColumn(name="training_id", nullable = false)
    private Training training;//Entrenamiento al que pertenece

    @OneToMany(mappedBy = "id.exercise",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
    private Set<GymSet> gymSets = new HashSet<>();//Series que contiene un ejercicio








    /*
     * Constructor básico para crear un Ejercicio en base a los datos enviados con el DTO
     */
    public Exercise(ExerciseDto exerciseDto){
        id = null; //Id siempre será nulo al crear un Ejercicio desde 0, será la BBDD quien lo establezca
        update(exerciseDto); //Se copian el resto de datos
    }







    /*
     * Convierte el ejercicio en DTO para mostrarlo en una vista
     * El ejercicio SIEMPRE debe devolver las series, ya que están estrechamente vinculadas en las vistas
     */
    public ExerciseDto getDto() {
        ExerciseDto exerciseDto = new ExerciseDto();

        //Se mapean todos los datos de la entidad al dto

        //Campos obligatorios que no pueden ser null
        exerciseDto.setId(id);
        exerciseDto.setTrainingId(training.getId());
        exerciseDto.setExerciseName(exerciseName.getName());
        exerciseDto.setExerciseOrder(exerciseOrder);
        

        //Campos opcionales, pueden ser null
        exerciseDto.setVariant(variant);
        exerciseDto.setExerciseComment(exerciseComment);


        // Ahora cargamos las series de este ejercicio en caso de que existan
        if (!gymSets.isEmpty()) {

            //Se transforma la lista de series a la lista de series dto y las ordena
            Set<GymSetDto> setsDto = gymSets.stream()
                    .map(gymSet -> gymSet.getDto())
                    .sorted(Comparator.comparing(GymSetDto::getSetOrder))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            //Finalmente se lo pasamos a la instacia de ejercicio dto
            exerciseDto.setGymSetsDtos(setsDto);
        }

        return exerciseDto;
    }







    /*
     * Actualiza los campos simples de esta entidad en base a los datos del Dto
     */
    public void update(ExerciseDto exerciseDto){


        //Si vienen vacios se pasan a null en la BBDD
        String v = exerciseDto.getVariant();
        variant = (v.isBlank())?  null : v;

        String c = exerciseDto.getExerciseComment();
        exerciseComment = (c.isBlank())?  null : c;

    }


}

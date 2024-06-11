package com.victor.project.gymapp.models;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private Integer id;

    //Orden del ejercicio en el contexto de un entrenamiento
    @Column(name = "exercise_order", nullable = false)
    private Byte exerciseOrder;

    @ManyToOne()//Un ejercicio puede tener un solo nombre, pero los nombres pueden estar en muchos ejercicios
    @JoinColumn(name="exercise_name_id", nullable = false)
    private ExerciseName exerciseName;

    @Column(length = 30)
    private String variant;//Un ejercicio puede tener variaciones o elementos adicionales

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ExerciseComment exerciseComment;//comentario asociado a un ejercicio

    @ManyToOne()
    @JoinColumn(name="training_id", nullable = false)
    private Training training;//Entrenamiento al que pertenece

    @OneToMany(mappedBy = "id.exercise",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
    private Set<GymSet> gymSets = new HashSet<>();//Series que contiene un ejercicio




    /**
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
        
        exerciseDto.setVariant(variant);//Se mapea sea nula o no

        if (exerciseComment != null)//Se verifica si existe antes de acceder a su contenido
            exerciseDto.setExerciseComment(exerciseComment.getComment());

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
}

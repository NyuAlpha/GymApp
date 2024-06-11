package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.Exercise;

public interface ExerciseRepository extends CrudRepository<Exercise,Integer>{

    //Carga de ejercicio manual para evitar consulas que solo lastran la aplicación
    //@EntityGraph(attributePaths = {"exerciseName", "exerciseComment", "training", "gymSets"})
    @Query("SELECT e FROM Exercise e " +
         "LEFT JOIN FETCH e.gymSets " +
         "WHERE e.id = :exerciseId")
    Optional<Exercise> findByIdWithDetails(@Param("exerciseId") Integer exerciseId);



    @Query("SELECT e.training.season.user.username FROM Exercise e WHERE e.id = :exerciseId")
    String findUsernameByExerciseId(@Param("exerciseId") Integer exerciseId);


    // consulta para contar los ejercicios por ID de entrenamiento
    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.training.id = :trainingId")
    Byte countByTrainingId(Integer trainingId);



    

    Optional<Exercise> findByTrainingIdAndExerciseOrder(Integer trainingId, byte exerciseOrder);

    
}

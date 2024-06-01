package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.Exercise;

public interface ExerciseRepository extends CrudRepository<Exercise,Long>{

    //Carga de ejercicio manual para evitar consulas que solo lastran la aplicación
    //@EntityGraph(attributePaths = {"exerciseName", "exerciseComment", "training", "gymSets"})
    @Query("SELECT e FROM Exercise e " +
         "LEFT JOIN FETCH e.training t " +
         "LEFT JOIN FETCH e.exerciseName " +
         "LEFT JOIN FETCH e.exerciseComment " +
         "LEFT JOIN FETCH e.gymSets " +
         "WHERE e.id = :id")
    Optional<Exercise> findByIdWithDetails(@Param("id") Long id);



    @Query("SELECT e.training.season.user.username FROM Exercise e WHERE e.id = :exerciseId")
    String findUsernameByExerciseId(@Param("exerciseId") Long exerciseId);
}

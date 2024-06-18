package com.victor.project.gymapp.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.victor.project.gymapp.models.Exercise;


/*
 * Repositorio para cargar ejercicios y datos relacionados desde la BBDD
 */
public interface ExerciseRepository extends CrudRepository<Exercise,Integer>{



    /*
     * Carga de ejercicio junto a sus series
     */
    @Query("SELECT e FROM Exercise e " +
         "LEFT JOIN FETCH e.gymSets " +
         "WHERE e.id = :exerciseId")
    Optional<Exercise> findByIdWithDetails(@Param("exerciseId") Integer exerciseId);






    /*
     * Cuenta los ejercicios por ID de entrenamiento
     */
    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.training.id = :trainingId")
    Byte countByTrainingId(Integer trainingId);



    
    /*
     * Carga un ejercicio en base al id de su entrenameinto padre y del orden que ocupa en la lista de ejercicios
     * para dicho entrenamiento
     */
    Optional<Exercise> findByTrainingIdAndExerciseOrder(Integer trainingId, byte exerciseOrder);

    
}

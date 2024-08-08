package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.models.GymSetId;


/*
 * Repositorio para las series de los ejercicios
 */
public interface GymSetRepository extends CrudRepository<GymSet,GymSetId>{



    /*
     * cuenta las series contenidas en el ejerccio por ID de ejercicio
     */
    @Query("SELECT COUNT(s) FROM GymSet s WHERE s.id.exercise.id = :exerciseId")
    Byte countByExerciseId(Integer exerciseId);


    /*
     * Borra una serie mediante el id compuesto
     */
    @Modifying
    @Query("DELETE FROM GymSet gs WHERE gs.id.exercise.id = :exerciseId AND gs.id.setOrder = :setOrder")
    void deleteByExerciseIdAndSetOrder(@Param("exerciseId") Integer exerciseId, @Param("setOrder") Byte setOrder);


    /*
     * Busca una serie mediante el id compuesto
     */
    @Query("SELECT gs FROM GymSet gs WHERE gs.id.exercise.id = :exerciseId AND gs.id.setOrder = :setOrder")
    Optional<GymSet> findByExerciseIdAndSetOrder(@Param("exerciseId") Integer exerciseId, @Param("setOrder") Byte setOrder);



    /*
     * Modifica el orden de la serie dentro del ejercicio padre
     */
    @Modifying
    @Query(value = "UPDATE gym_sets SET set_order = :newOrder " + 
                    "WHERE exercise_id = :exerciseId AND set_order = :currentOrder"
                    , nativeQuery = true)
    void updateSetOrder(@Param("exerciseId") Integer exerciseId, @Param("currentOrder") Byte currentOrder, @Param("newOrder") Byte newOrder);
    



    /*
     * Devuelve la última serie si existe, utiliza query nativo
     * para poder acceder al último en una sola consulta
     */
    @Query("SELECT gs FROM GymSet gs WHERE gs.id.exercise.id = :exerciseId AND gs.id.setOrder = (SELECT MAX(gs2.id.setOrder) FROM GymSet gs2 WHERE gs2.id.exercise.id = :exerciseId)")
    Optional<GymSet> findLastByExerciseId(@Param("exerciseId") Integer exerciseId);


    /*
     * Este método sirve para reestablecer el orden de las series cuando una es eliminada, bajando la 
     * posición de cada serie posterior una unidad para que no queden posiciones vacias.
     */
    @Modifying
    @Query("UPDATE GymSet gs SET gs.id.setOrder = gs.id.setOrder - 1 WHERE " 
            + "gs.id.exercise.id = :exerciseId AND "
            + "gs.id.setOrder > :setOrder")
    void updateSetOrder(@Param("exerciseId") Integer exerciseId, @Param("setOrder") Byte setOrder);


}

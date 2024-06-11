package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.models.GymSetId;

public interface GymSetRepository extends CrudRepository<GymSet,GymSetId>{


    // consulta para contar las series por ID de ejercicio
    @Query("SELECT COUNT(s) FROM GymSet s WHERE s.id.exercise.id = :exerciseId")
    Byte countByExerciseId(Integer exerciseId);


    //Borra mediante el id compuesto
    @Modifying
    @Query("DELETE FROM GymSet gs WHERE gs.id.exercise.id = :exerciseId AND gs.id.setOrder = :setOrder")
    void deleteByExerciseIdAndSetOrder(@Param("exerciseId") Integer exerciseId, @Param("setOrder") Byte setOrder);


    //Buesca mediante el id compuesto
    @Query("SELECT gs FROM GymSet gs WHERE gs.id.exercise.id = :exerciseId AND gs.id.setOrder = :setOrder")
    Optional<GymSet> findByExerciseIdAndSetOrder(@Param("exerciseId") Integer exerciseId, @Param("setOrder") Byte setOrder);


    @Modifying
    @Query(value = "UPDATE gym_sets SET set_order = :newOrder WHERE exercise_id = :exerciseId AND set_order = :currentOrder", nativeQuery = true)
    void updateSetOrder(@Param("exerciseId") Integer exerciseId, @Param("currentOrder") Byte currentOrder, @Param("newOrder") Byte newOrder);
    
}

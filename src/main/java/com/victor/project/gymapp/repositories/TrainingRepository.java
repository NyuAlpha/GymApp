package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.Training;

public interface TrainingRepository extends JpaRepository<Training,Integer>{


    //Obtiene todos los datos de un entrenamiento, su temporada, comentarios y sus ejercicios de forma anidada.
    @Query("SELECT t FROM Training t " +
        "LEFT JOIN FETCH t.trainingComment tc " +
        "LEFT JOIN FETCH t.exercises e " +
        "LEFT JOIN FETCH e.exerciseName en " +
        "LEFT JOIN FETCH e.exerciseComment ec " +
        "LEFT JOIN FETCH t.season " +
        "LEFT JOIN FETCH e.gymSets " +
        "WHERE t.id = :id ")
    Optional<Training> findByIdWithDetails(@Param("id") Integer id);


    //Busca todas los entrenamientos de una temporada y los pagina
    @Query("SELECT t FROM Training t LEFT JOIN FETCH t.season s WHERE s.id = :id ORDER BY t.date DESC")
    Page<Training> findBySeasonId(Pageable pageable, @Param("id") Integer seasonId);
}

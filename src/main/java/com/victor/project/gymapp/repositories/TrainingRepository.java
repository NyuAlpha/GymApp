package com.victor.project.gymapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.Training;

public interface TrainingRepository extends JpaRepository<Training,Long>{


    @Query("SELECT t FROM Training t " +
        "LEFT JOIN FETCH t.exercises e " +
        "LEFT JOIN FETCH t.season " +
        "LEFT JOIN FETCH e.gymSets " +
        "WHERE t.id = :id ")
    Optional<Training> findByIdWithDetails(@Param("id") Long id);


    //Busca todas los entrenamientos de una temporada y los pagina
    @Query("SELECT t FROM Training t LEFT JOIN FETCH t.season s WHERE s.id = :id")
    Page<Training> findBySeasonId(Pageable pageable, @Param("id") Long seasonId);
}

package com.victor.project.gymapp.repositories;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.Season;

public interface SeasonRepository extends CrudRepository<Season,Integer>{

    //Devuelve las temporadas de un usuario de forma paginada
    @Query("SELECT s FROM Season s " + 
    "LEFT JOIN FETCH s.seasonComment " +
    "LEFT JOIN FETCH s.user u " +
    "WHERE u.uuid = :uuid")
    Page<Season> findByUserUuid(@Param("uuid") String uuid, Pageable pageable);



    @Query("SELECT s FROM Season s " + 
    "LEFT JOIN FETCH s.seasonComment " +
    "WHERE s.id = :seasonId")
    Optional<Season> findSeasonByIdWithComment(@Param("seasonId") Integer seasonId);


}

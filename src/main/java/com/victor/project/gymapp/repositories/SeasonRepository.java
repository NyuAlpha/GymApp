package com.victor.project.gymapp.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.Season;


/*
 * Repositorio de temporadas de entrenamiento
 */
public interface SeasonRepository extends CrudRepository<Season,Integer>{

    //Devuelve las temporadas de un usuario de forma paginada
    @Query("SELECT s FROM Season s WHERE s.user.uuid = :uuid")
    Page<Season> findByUserUuid(@Param("uuid") String uuid, Pageable pageable);


}

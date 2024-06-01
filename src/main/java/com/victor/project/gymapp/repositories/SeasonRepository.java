package com.victor.project.gymapp.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.victor.project.gymapp.models.Season;

public interface SeasonRepository extends CrudRepository<Season,Long>{

    //Devuelve las temporadas de un usuario de forma paginada
    Page<Season> findByUserUuid(String username, Pageable pageable);


}

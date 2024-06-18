package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.dto.SeasonDto;
import com.victor.project.gymapp.models.Season;

/*
 * Interfaz de service para manipulación de temporadas
 */
public interface ISeasonService {

    //Busca de forma paginada todas las temporadas
    Page<Season> findAllSeasons(Pageable pageable);

    //Guarda y devuelve una temporada en la base de datos a aprtir de un dto
    Season saveSeason(SeasonDto seasonDto);

    //Obtiene la temporada en base a su id
    Season getSeason(Integer seasonId);

    //Actualiza y devuelve la temporada en base a su id
    Season updateSeason(SeasonDto seasonDto);

    //Borra la temporada en base a su id
    void deleteSeason(Integer seasonId);

}

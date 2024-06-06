package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.dto.SeasonDto;
import com.victor.project.gymapp.models.Season;

public interface ISeasonService extends CrudService {

    Page<Season> findAllSeasons(Pageable pageable);

    Season saveSeason(SeasonDto seasonDto);

    Season getSeason(Long seasonId);

    Season getSeasonWithComment(Long seasonId);

    Season updateSeason(SeasonDto seasonDto, Long seasonId);

    void deleteSeason(Long seasonId);

}

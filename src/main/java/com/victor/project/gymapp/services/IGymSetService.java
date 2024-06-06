package com.victor.project.gymapp.services;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.models.GymSet;

public interface IGymSetService extends CrudService {

    void saveGymSet(GymSetDto setDto);

    void deleteGymSet(Long id);

    GymSet updateGymSet(GymSetDto gymSetDto);
}

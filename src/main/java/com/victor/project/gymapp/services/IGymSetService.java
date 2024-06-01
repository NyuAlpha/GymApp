package com.victor.project.gymapp.services;

import com.victor.project.gymapp.models.GymSet;

import dto.GymSetDto;

public interface IGymSetService extends CrudService{

    void saveGymSet(GymSetDto setDto);
    void deleteGymSet(Long id);
    GymSet updateGymSet(GymSetDto gymSetDto);
}

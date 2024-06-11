package com.victor.project.gymapp.services;

import com.victor.project.gymapp.dto.GymSetDto;

public interface IGymSetService extends CrudService {

    void saveGymSet(GymSetDto setDto);

    void deleteGymSet(Integer exerciseId, Byte order);

    void updateGymSet(GymSetDto gymSetDto);

    void up(Integer exerciseId, Byte setOrder);

    void down(Integer exerciseId, Byte setOrder);
}

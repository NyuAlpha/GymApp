package com.victor.project.gymapp.services;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.models.GymSet;



/*
 * Interfaz de service para manipulación de series
 */
public interface IGymSetService {

    void saveGymSet(GymSetDto setDto);

    void deleteGymSet(Integer exerciseId, Byte order);

    void updateGymSet(GymSetDto gymSetDto);

    void up(Integer exerciseId, Byte setOrder);

    void down(Integer exerciseId, Byte setOrder);

    GymSet getLastGymSet(Integer exerciseId);
}

package com.victor.project.gymapp.services;

import com.victor.project.gymapp.dto.UserDto;

/*
 * Interfaz de service para manipulación de usuarios
 */
public interface IUserService {



    String getCurrentUserUuid();


    boolean checkUserForSeasonId(Integer seasonId);
    boolean checkUserForUserRecordId(Integer userRecordId);
    boolean checkUserForTraining(Integer trainingId);
    boolean checkUserForExercise(Integer exerciseId);
    void createUser(UserDto userDto);

    boolean existByName(String name);
    boolean existByEmail(String email);
}

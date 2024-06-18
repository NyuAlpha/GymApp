package com.victor.project.gymapp.services;


/*
 * Interfaz de service para manipulación de usuarios
 */
public interface IUserService {



    String getCurrentUserUuid();


    boolean checkUserForSeasonId(Integer seasonId);
    boolean checkUserForUserRecordId(Integer userRecordId);
    boolean checkUserForTraining(Integer trainingId);
    boolean checkUserForExercise(Integer exerciseId);
}

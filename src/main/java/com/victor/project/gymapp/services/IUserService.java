package com.victor.project.gymapp.services;

public interface IUserService {

    String getCurrentUsername();
    String getCurrentUserUuid();


    boolean checkUserForSeasonId(Integer seasonId);
    boolean checkUserForUserRecordId(Integer userRecordId);
}

package com.victor.project.gymapp.services;

public interface IUserService {

    String getCurrentUsername();
    String getCurrentUserUuid();


    boolean checkUserForSeasonId(Long seasonId);
}

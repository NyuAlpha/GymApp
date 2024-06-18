package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.dto.UserRecordDto;
import com.victor.project.gymapp.models.UserRecord;




/*
 * Interfaz de service para manipulación de registros de usuario
 */
public interface IUserRecordService {

    Page<UserRecord> findAllUserRecord(Pageable pageable);

    UserRecord saveUserRecord(UserRecordDto userRecordDto);

    UserRecord getUserRecord(Integer userRecordId);

    UserRecord updateUserRecord(UserRecordDto userRecordDto);

    void deleteUserRecord(Integer userRecordId);

    UserRecord getLastUserRecordByUser();
}

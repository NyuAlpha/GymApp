package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.dto.UserRecordDto;
import com.victor.project.gymapp.models.UserRecord;

public interface IUserRecordService extends CrudService {

    Page<UserRecord> findAllUserRecord(Pageable pageable);

    UserRecord saveUserRecord(UserRecordDto userRecordDto);

    UserRecord getUserRecord(Long userRecordId);

    UserRecord updateUserRecord(UserRecordDto userRecordDto, Long userRecordId);

    void deleteUserRecord(Long userRecordId);

    UserRecord getLastUserRecordByUser();
}

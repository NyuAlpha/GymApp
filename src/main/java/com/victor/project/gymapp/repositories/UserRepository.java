package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.User;

public interface UserRepository extends CrudRepository<User, String> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByUuid(String uuid);

    public boolean existsByUsername(String username);

    public boolean existsByPassword(String password);

    // Devuelve el uuid del propietario de la temporada
    @Query("SELECT s.user.uuid FROM Season s WHERE s.id = :seasonId")
    Optional<String> findUserUuidBySeason(@Param("seasonId") Integer seasonId);

    // Devuelve el uuid del propietario del registro de usuario
    @Query("SELECT r.user.uuid FROM UserRecord r WHERE r.id = :userRecordId")
    Optional<String> findUserUuidByUserRecord(@Param("userRecordId") Integer userRecordId);

}

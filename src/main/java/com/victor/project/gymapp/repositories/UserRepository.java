package com.victor.project.gymapp.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.User;

public interface UserRepository extends CrudRepository<User,String>{

    public Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);

    public boolean existsByPassword(String password);


    @Query("SELECT s.user.uuid FROM Season s WHERE s.id = :seasonId")
    Optional<String> findUserUuidBySeason(@Param("seasonId") Long seasonId);

}

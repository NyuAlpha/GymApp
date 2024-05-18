package com.victor.project.gymapp.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.victor.project.gymapp.models.User;

public interface UserRepository extends CrudRepository<User,String>{

    public Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);

    public boolean existsByPassword(String password);

}

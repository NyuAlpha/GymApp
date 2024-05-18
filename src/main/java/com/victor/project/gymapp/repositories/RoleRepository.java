package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.victor.project.gymapp.models.Role;

public interface RoleRepository extends CrudRepository<Role,Long>{

    public Optional<Role> findByName(String name);

}

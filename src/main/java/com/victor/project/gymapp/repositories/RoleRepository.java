package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.victor.project.gymapp.models.Role;

/*
 * Repositorio de roles de usuario
 */
public interface RoleRepository extends CrudRepository<Role,Integer>{

    public Optional<Role> findByName(String name);

}

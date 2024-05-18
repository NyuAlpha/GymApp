package com.victor.project.gymapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.victor.project.gymapp.models.GymSet;

public interface SetRepository extends CrudRepository<GymSet,Long>{

}

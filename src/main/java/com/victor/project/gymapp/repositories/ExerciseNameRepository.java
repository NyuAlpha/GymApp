package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.victor.project.gymapp.models.ExerciseName;


public interface ExerciseNameRepository extends CrudRepository<ExerciseName,Integer>{

    public Optional<ExerciseName> findByName(String name);
}

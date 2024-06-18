package com.victor.project.gymapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.victor.project.gymapp.models.ExerciseName;

/*
 * Repositorio de nombres de ejercicio
 */
public interface ExerciseNameRepository extends CrudRepository<ExerciseName,Integer>{

    
    public Optional<ExerciseName> findByName(String name);

    /*
     * encuentra los ejercicios que empiecen por el token enviado como argumento
     */
    @Query("SELECT en.name FROM ExerciseName en WHERE en.name LIKE :token%")
    public List<String> findAllLikeName(String token);
}

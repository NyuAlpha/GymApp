package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.victor.project.gymapp.models.User;




/*
 * Repositorio de usuarios de la aplicación
 */
public interface UserRepository extends CrudRepository<User, String> {



    
    public Optional<User> findByUsername(String username);



    public Optional<User> findByUuid(String uuid);



    public boolean existsByUsername(String username);

    public boolean existsByEmail(String name);



    /*
     * Devuelve el uuid del propietario de una temporada
     */
    @Query("SELECT s.user.uuid FROM Season s WHERE s.id = :seasonId")
    Optional<String> findUserUuidBySeason(@Param("seasonId") Integer seasonId);



    /*
     * Devuelve el uuid del propietario de un registro de usuario
     */
    @Query("SELECT r.user.uuid FROM UserRecord r WHERE r.id = :userRecordId")
    Optional<String> findUserUuidByUserRecord(@Param("userRecordId") Integer userRecordId);


    /*
     * Devuelve el uuid del propietario de un entrenamiento
     */
    @Query("SELECT t.season.user.uuid FROM Training t WHERE t.id = :trainingId")
    public Optional<String> findUserUuidByTraining(Integer trainingId);


    /*
     * Devuelve el uuid del propietario de un ejercicio
     */
    @Query("SELECT e.training.season.user.uuid FROM Exercise e WHERE e.id = :exerciseId")
    public Optional<String> findUserUuidByExercise(Integer exerciseId);

}

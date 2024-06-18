package com.victor.project.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.victor.project.gymapp.models.UserRecord;


/*
 * Repositorio para registros de estadisticas físicas de usuario
 */
public interface UserRecordRepository extends CrudRepository<UserRecord, Integer> {



    /*
     * Devuelve los registros de un usuario de forma paginada
     */
    Page<UserRecord> findByUserUuid(String uuid, Pageable pageable);




    
    /*
     * Devuelve el último registro de un usuario si existe, utiliza query nativo
     * para poder acceder al último en una sola consulta
     */
    @Query(value = "SELECT * FROM user_records " +
                "WHERE user_id = :userRecordId " + 
                "ORDER BY date DESC LIMIT 1"
                , nativeQuery = true)
    public Optional<UserRecord> findLastByUuid(@Param("userRecordId") String userRecordId);
}

package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.UserRecordDto;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.models.UserRecord;
import com.victor.project.gymapp.repositories.UserRecordRepository;
import com.victor.project.gymapp.repositories.UserRepository;
import lombok.AllArgsConstructor;



/*
 * Servicio para manipular y procesar registros de control de usuario
 */
@Service
@AllArgsConstructor
public class UserRecordService implements IUserRecordService {

    //Repositorios necesarios
    private UserRepository userRepository;
    private UserRecordRepository userRecordRepository;
    private UserService userService;





    /*
     * Recupera los registros del usuario logeado de forma paginada
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRecord> findAllUserRecord(Pageable pageable) {
        return userRecordRepository.findByUserUuid(userService.getCurrentUserUuid(), pageable);
    }








    /*
     * Crea y guarda un nuevo registro de usuario en base al dto, luego lo devuelve.
     */
    @Override
    @Transactional
    public UserRecord saveUserRecord(UserRecordDto userRecordDto) {

        // El id debe estar vacio cuando creamos el registro por primera vez, por lo que nos aseguramos.
        userRecordDto.setId(null);

        // Este constructor transforma el Dto a entidad de BBDD
        UserRecord userRecord = new UserRecord(userRecordDto);

        // Obtiene el usuario de la sesión actual para asignarseo al registro
        User user = userRepository.findByUuid(userService.getCurrentUserUuid())
                .orElseThrow(() -> new NoSuchElementException("Usuario actual no encontrado"));
        userRecord.setUser(user);

        // Se guarda y se devuelve
        return userRecordRepository.save(userRecord);
    }








    /*
     * Recupera un registro de usuario mediante el id
     */
    @Override
    @Transactional(readOnly = true)
    public UserRecord getUserRecord(Integer userRecordId) {
        return userRecordRepository.findById(userRecordId).orElseThrow(NoSuchElementException::new);
    }








    
    /*
     * Actualiza el registro de usuario por su id, copiando los datos del dto, luego lo devuelve
     */
    @Override
    @Transactional
    public UserRecord updateUserRecord(UserRecordDto userRecordDto) {
        // Obtiene el usuario
        UserRecord userRecord = userRecordRepository.findById(userRecordDto.getId()).orElseThrow(NoSuchElementException::new);
        // Actualiza con los nuevos campos
        userRecord.update(userRecordDto);
        // Se actualiza y se devuelve
        return userRecordRepository.save(userRecord);
    }









    // Elimina el registro de usuario por su id
    @Override
    @Transactional
    public void deleteUserRecord(Integer userRecordId) {
        userRecordRepository.deleteById(userRecordId);// Lo borra
    }








    /*
     * Devuelve el último registro de usuario si hay
     */
    public UserRecord getLastUserRecordByUser() {
        return userRecordRepository.findLastByUuid(userService.getCurrentUserUuid()).orElse(new UserRecord());
    }

}

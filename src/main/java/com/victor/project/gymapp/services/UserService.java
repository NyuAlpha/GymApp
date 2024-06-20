package com.victor.project.gymapp.services;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.UserDto;
import com.victor.project.gymapp.models.Role;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.RoleRepository;
import com.victor.project.gymapp.repositories.UserRepository;
import com.victor.project.gymapp.security.CustomUserDetails;

import lombok.AllArgsConstructor;




/*
 * Servicio para manipular y procesar usuarios
 */
@Service
@AllArgsConstructor
public class UserService implements IUserService {

    //Repositorios necesario
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    //Necesario para crear usuarios
    private BCryptPasswordEncoder bCryptPasswordEncoder;






    /*
     * Retorna el UUID de usuario que está logeado el cual realiza la petición
     */
    @Override
    @Transactional
    public String getCurrentUserUuid() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUuid();
        } else {
            throw new IllegalStateException("Principal is not an instance of CustomUserDetails");
        }
    }







    /*
     * Verifica que la temporada pertenece al usuario logeado
     */
    @Override
    @Transactional
    public boolean checkUserForSeasonId(Integer seasonId) {
        Optional<String> optionalUuid = userRepository.findUserUuidBySeason(seasonId);
        return checkUser(optionalUuid);
    }





    /*
     * Verifica que el registro de usuario pertenece al usuario logeado
     */
    @Override
    @Transactional
    public boolean checkUserForUserRecordId(Integer userRecordId) {
        Optional<String> optionalUuid = userRepository.findUserUuidByUserRecord(userRecordId);
        return checkUser(optionalUuid);
    }




    /*
     * Verifica que el entrenamiento pertenece al usuario logeado
     */
    @Override
    @Transactional
    public boolean checkUserForTraining(Integer trainingId) {
        Optional<String> optionalUuid = userRepository.findUserUuidByTraining(trainingId);
        return checkUser(optionalUuid);
    }




    /*
     * Verifica que el entrenamiento pertenece al usuario logeado
     */
    @Override
    @Transactional
    public boolean checkUserForExercise(Integer exerciseId) {
        Optional<String> optionalUuid = userRepository.findUserUuidByExercise(exerciseId);
        return checkUser(optionalUuid);
    }




    /*
     *  Método común para comparar uuid , compara el UUID del propietario de un registro con el del usuario logeado
     *  si son el mismo devolverá true, si no, false.
     */
    @Transactional
    private boolean checkUser(Optional<String> optionalUuid) {


        // Si no existe el uuid se queda vacio
        String uuid = optionalUuid.orElse("");
        // Se obtiene el uuid del usuario logeado
        String actualUserUuid = getCurrentUserUuid();
        // retorna true o false dependiendo de si coinciden o no.
        return uuid.equals(actualUserUuid);
    }





    /*
     * Crea un nuevo usuario, y lo logea automáticamente
     * Este método es llamado desde el Sign up
     */
    @Override
    @Transactional
    public void createUser(UserDto userDto) {

        //Mapea el userDto a una entidad usuario y encripta la contraseña
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // Almacenar la contraseña en texto plano antes de encriptarla
        String rawPassword = userDto.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        
        //Asigna un rol de usuario por defecto
        Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER");
        userRoleOptional.ifPresent(role -> user.addRole(role));

        //Persistencia del nuevo usuario
        userRepository.save(user);

    }


    /*
     * Comprueba si existe un usuario con ese nombre
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existByName(String username){
        return userRepository.existsByUsername(username);
    }


    /*
     * Comprueba si existe un usuario con ese email
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existByEmail(String email){
        return userRepository.existsByEmail(email);
    }

}

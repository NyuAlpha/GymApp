package com.victor.project.gymapp.services;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.repositories.UserRepository;
import com.victor.project.gymapp.security.CustomUserDetails;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public String getCurrentUserUuid() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUuid();
        } else {
            throw new IllegalStateException("Principal is not an instance of CustomUserDetails");
        }
    }

    // Verifica que la temporada pertenece al usuario logeado
    @Override
    @Transactional
    public boolean checkUserForSeasonId(Integer seasonId) {
        Optional<String> optionalUuid = userRepository.findUserUuidBySeason(seasonId);
        return checkUser(optionalUuid);
    }

    // Verifica que el registro de usuario pertenece al usuario logeado
    @Override
    @Transactional
    public boolean checkUserForUserRecordId(Integer userRecordId) {
        Optional<String> optionalUuid = userRepository.findUserUuidByUserRecord(userRecordId);
        return checkUser(optionalUuid);
    }

    // Método común para comparar uuid
    // Compara el UUID del propietario de un registro con el del usuario logeado
    private boolean checkUser(Optional<String> optionalUuid) {


        // Si no existe el uuid se queda vacio
        String uuid = optionalUuid.orElse("");
        // Se obtiene el uuid del usuario logeado
        String actualUserUuid = getCurrentUserUuid();
        // retorna true o false dependiendo de si coinciden o no.
        return uuid.equals(actualUserUuid);
    }

}

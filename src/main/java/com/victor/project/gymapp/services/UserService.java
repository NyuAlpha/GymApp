package com.victor.project.gymapp.services;

import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.repositories.UserRepository;

import security.CustomUserDetails;

@Service
public class UserService  implements IUserService{

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

    @Override
    @Transactional
    public boolean checkUserForSeasonId(Long seasonId) {
        Optional<String> optionalUuid = userRepository.findUserUuidBySeason(seasonId);
        String uuid = optionalUuid.orElseThrow(() -> new AccessDeniedException(""));

        String actualUserUuid = getCurrentUserUuid();

        if (!uuid.equals(actualUserUuid)){
            throw new AccessDeniedException("");
        }
        return true;
    }
 


}

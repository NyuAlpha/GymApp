package com.victor.project.gymapp.services;

import org.springframework.stereotype.Service;

import com.victor.project.gymapp.repositories.UserRepository;

@Service
public class AuthServiceImp implements AuthService{

    private UserRepository userRepository;

    public AuthServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public boolean exitsUsernamePassword(String username, String password) {
        return userRepository.existsByUsername(username) && userRepository.existsByPassword(password);
    }

}

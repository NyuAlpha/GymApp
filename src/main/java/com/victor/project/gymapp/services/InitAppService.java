package com.victor.project.gymapp.services;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.victor.project.gymapp.models.Role;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.RoleRepository;
import com.victor.project.gymapp.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

/*
 * Esta clase es para pruebas, su utiliza es crear usuarios y/o obtener contraseñas encriptadas.
 */
@Component
@Profile("dev")
public class InitAppService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @PostConstruct
    public void createRoles(){

        //Se deshabilita para que no intente insertar datos cada vez que se ejecuta al app
        boolean createMode = true;

        if(createMode){
            createRole("ROLE_ADMIN");
            createRole("ROLE_USER");
            createAdmin();
            createUser();
        }

    }



    private void createRole(String roleName){
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if(roleOptional.isEmpty()){
            roleRepository.save(new Role(null, roleName));
        }
    }




    private User createAdmin(){
        Optional<User> userOptional = userRepository.findByUsername("admin");
        if(userOptional.isEmpty()){
            User user = new User("admin", "super@gmail.com", bCryptPasswordEncoder.encode("123456"));
            Optional<Role> adminroleOptional = roleRepository.findByName("ROLE_ADMIN");
            Optional<Role> userroleOptional = roleRepository.findByName("ROLE_USER");
            
            adminroleOptional.ifPresent(role -> user.addRole(role));
            userroleOptional.ifPresent(role -> user.addRole(role));

            return userRepository.save(user);
        }
        else{
            return userOptional.get();
        }
    }



    
    private User createUser(){
        Optional<User> userOptional = userRepository.findByUsername("user");
        if(userOptional.isEmpty()){
            User user = new User("user", "user@gmail.com", bCryptPasswordEncoder.encode("123456"));
            Optional<Role> userroleOptional = roleRepository.findByName("ROLE_USER");
            
            userroleOptional.ifPresent(role -> user.addRole(role));

            return userRepository.save(user);
        }
        else{
            return userOptional.get();
        }
    }

}

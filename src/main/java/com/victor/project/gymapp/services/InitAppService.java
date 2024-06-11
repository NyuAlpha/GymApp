package com.victor.project.gymapp.services;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.victor.project.gymapp.models.Role;
import com.victor.project.gymapp.models.Season;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.RoleRepository;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

/*
 * Esta clase tiene la unica intención de crear 
 */
@Component
public class InitAppService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @PostConstruct
    public void createRoles(){
        createRole("ROLE_ADMIN");
        createRole("ROLE_USER");
        createAdmin();
        User user = createUser();
        //insertDataTest(user);
        //insertSeason(user);
    }


    private void insertSeason(User user){


        Season season = new Season();
        season.setUser(user);
        season.setStartDate(LocalDate.now());
        season.setTitle("Temporada prueba");

        seasonRepository.save(season);
    }

    private void createRole(String roleName){
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if(roleOptional.isEmpty()){
            roleRepository.save(new Role(null, roleName));
        }
    }

    private User createAdmin(){
        Optional<User> userOptional = userRepository.findByUsername("superadmin");
        if(userOptional.isEmpty()){
            User user = new User("superadmin", "super@gmail.com", bCryptPasswordEncoder.encode("123456"));
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

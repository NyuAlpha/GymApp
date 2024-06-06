package com.victor.project.gymapp.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.ExerciseComment;
import com.victor.project.gymapp.models.ExerciseName;
import com.victor.project.gymapp.models.Role;
import com.victor.project.gymapp.models.Season;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.models.TrainingComment;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.ExerciseNameRepository;
import com.victor.project.gymapp.repositories.RoleRepository;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;
import com.victor.project.gymapp.repositories.UserRecordRepository;
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
    private UserRecordRepository userDetailsRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private ExerciseNameRepository exerciseNameRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @PostConstruct
    public void createRoles(){
        createRole("ROLE_ADMIN");
        createRole("ROLE_USER");
        User admin = createAdmin();
        User user = createUser();
        //insertDataTest(user);
        insertSeason(user);
    }

    // private void insertDataTest(User user) {
    //     Training training = new Training(null, LocalDate.of(2024, 5, 12),"Entrenamiento de pecho", new TrainingComment(null, "Pecho"), new HashSet<>(), null, user);
        
    //     ExerciseName exerciseName1 = exerciseNameRepository.save(new ExerciseName(null, "Press de banca"));
    //     ExerciseName exerciseName2 = exerciseNameRepository.save(new ExerciseName(null, "Fondos"));

    //     Exercise exercise1 = new Exercise(null, exerciseName1, null, new ExerciseComment(null, "Fatiga en el triceps"), training, null);
    //     Exercise exercise2 = new Exercise(null,exerciseName2, null, new ExerciseComment(null, "(dejando 2 de rir)"), training, null);
    //     training.getExercises().add(exercise1);
    //     training.getExercises().add(exercise2);

    //     trainingRepository.save(training);

    //     List<Training> trainings = Arrays.asList(
    //         new Training(null, LocalDate.of(2024, 5, 13),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 14),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 15),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 16),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 17),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 18),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 19),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 20),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 21),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 22),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 23),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 24),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 25),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 26),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 27),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 28),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 29),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 30),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 5, 31),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
    //         new Training(null, LocalDate.of(2024, 6, 1),"Entrenamiento de pecho", new TrainingComment(null, "Pierna"), new HashSet<>(), null, user)
    //         );

    //     trainingRepository.saveAll(trainings);
    // }

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

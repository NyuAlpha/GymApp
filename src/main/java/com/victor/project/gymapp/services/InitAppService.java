package com.victor.project.gymapp.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.ExerciseComment;
import com.victor.project.gymapp.models.ExerciseName;
import com.victor.project.gymapp.models.Role;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.models.TrainingComment;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.ExerciseNameRepository;
import com.victor.project.gymapp.repositories.RoleRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;
import com.victor.project.gymapp.repositories.UserDetailsRepository;
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
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private ExerciseNameRepository exerciseNameRepository;

    @Transactional
    @PostConstruct
    public void createRoles(){
        createRole("ROLE_ADMIN");
        createRole("ROLE_USER");
        User user = createAdmin();
        insertDataTest(user);
    }

    private void insertDataTest(User user) {
        Training training = new Training(null, LocalDate.of(2024, 5, 12), new TrainingComment(null, "Pecho"), new HashSet<>(), null, user);
        
        ExerciseName exerciseName1 = exerciseNameRepository.save(new ExerciseName(null, "Press de banca"));
        ExerciseName exerciseName2 = exerciseNameRepository.save(new ExerciseName(null, "Fondos"));

        Exercise exercise1 = new Exercise(null, exerciseName1, null, new ExerciseComment(null, "Fatiga en el triceps"), training, null);
        Exercise exercise2 = new Exercise(null,exerciseName2, null, new ExerciseComment(null, "(dejando 2 de rir)"), training, null);
        training.getExercises().add(exercise1);
        training.getExercises().add(exercise2);

        trainingRepository.save(training);

        List<Training> trainings = Arrays.asList(
            new Training(null, LocalDate.of(2024, 5, 13), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 14), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 15), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 16), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 17), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 18), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 19), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 20), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 21), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 22), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 23), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 24), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 25), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 26), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 27), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 28), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 29), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 30), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 5, 31), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user),
            new Training(null, LocalDate.of(2024, 6, 1), new TrainingComment(null, "Pierna"), new HashSet<>(), null, user)
            );

        trainingRepository.saveAll(trainings);
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
            User user = new User("superadmin", "super@gmail.com", "123456");
            Optional<Role> adminroleOptional = roleRepository.findByName("ROLE_ADMIN");
            
            adminroleOptional.ifPresent(role -> user.addRole(role));

            return userRepository.save(user);
        }
        else{
            return userOptional.get();
        }
    }

}

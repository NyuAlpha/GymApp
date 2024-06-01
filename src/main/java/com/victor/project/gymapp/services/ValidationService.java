package com.victor.project.gymapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.GymSetRepository;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;
import com.victor.project.gymapp.repositories.UserRepository;

@Service
public class ValidationService {

    @Autowired
    private GymSetRepository gymSetRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private UserRepository userRepository;

    // public boolean validateSeriesOwnership(Long seriesId) {
    //     GymSet gymSet= gymSetRepository.findById(seriesId)
    //         .orElseThrow(() -> new RuntimeException("Series not found"));
    //     return validateExerciseOwnership(series.getExercise().getId());
    // }

    // public boolean validateExerciseOwnership(Long exerciseId) {
    //     Exercise exercise = exerciseRepository.findById(exerciseId)
    //         .orElseThrow(() -> new RuntimeException("Exercise not found"));
    //     return validateTrainingOwnership(exercise.getTraining().getId());
    // }

    // public boolean validateTrainingOwnership(Long trainingId) {
    //     Training training = trainingRepository.findById(trainingId)
    //         .orElseThrow(() -> new RuntimeException("Training not found"));
    //     return validateSeasonOwnership(training.getSeason().getId());
    // }

    // public boolean validateSeasonOwnership(Long seasonId) {
    //     Season season = seasonRepository.findById(seasonId)
    //         .orElseThrow(() -> new RuntimeException("Season not found"));
    //     return validateUserOwnership(season.getUser().getId());
    // }

    // public boolean validateUserOwnership(Long userId) {
    //     String currentUsername = getCurrentUsername();
    //     User user = userRepository.findById(userId)
    //         .orElseThrow(() -> new RuntimeException("User not found"));
    //     return user.getUsername().equals(currentUsername);
    // }

    // private String getCurrentUsername() {
    //     Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //     if (principal instanceof UserDetails) {
    //         return ((UserDetails) principal).getUsername();
    //     } else {
    //         return principal.toString();
    //     }
    // }
}
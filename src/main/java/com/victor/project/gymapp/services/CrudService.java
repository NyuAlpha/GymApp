package com.victor.project.gymapp.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.Training;

import dto.ExerciseDto;
import dto.TrainingDto;

public interface CrudService {

    Page<Training> findAllTrainingsWithComment(Pageable pageable);
    Training saveTraining(TrainingDto trainingDto);
    void deleteTraining(Long id);
    TrainingDto getFullTrainingById(Long id);
    Training updateTraining(TrainingDto trainingDto);
    void saveExercise(ExerciseDto exerciseDto);

    Optional<Exercise> getFullExerciseById(Long id);
    
}

package com.victor.project.gymapp.services;

import org.springframework.http.ResponseEntity;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.models.Exercise;

public interface IExerciseService extends CrudService {

    Exercise saveExercise(ExerciseDto exerciseDto);

    void deleteExercise(Integer id);

    Exercise getFullExerciseById(Integer id);

    Exercise updateExercise(ExerciseDto exerciseDto);

    void up(Integer exerciseId, Integer trainingId);

    void down(Integer exerciseId, Integer trainingId);

    ResponseEntity<?> getExercise(Integer exerciseId);
}

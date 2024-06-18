package com.victor.project.gymapp.services;

import java.util.List;
import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.models.Exercise;



/*
 * Interfaz de service para manipulación de ejercicios
 */
public interface IExerciseService{

    Exercise saveExercise(ExerciseDto exerciseDto);

    void deleteExercise(Integer id);

    Exercise getExerciseById(Integer id);

    Exercise updateExercise(ExerciseDto exerciseDto);

    void up(Integer exerciseId, Integer trainingId);

    void down(Integer exerciseId, Integer trainingId);

    List<String> getExerciseNames(String token);
}

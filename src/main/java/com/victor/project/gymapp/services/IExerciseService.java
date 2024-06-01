package com.victor.project.gymapp.services;

import com.victor.project.gymapp.models.Exercise;

import dto.ExerciseDto;

public interface IExerciseService extends CrudService{

    void saveExercise(ExerciseDto exerciseDto);
    void deleteExercise(Long id);
    ExerciseDto getFullExerciseById(Long id);
    Exercise updateExercise(ExerciseDto exerciseDto);
}

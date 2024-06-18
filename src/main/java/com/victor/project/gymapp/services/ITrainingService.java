package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.dto.TrainingDto;
import com.victor.project.gymapp.models.Training;


/*
 * Interfaz de service para manipulación de entrenamientos
 */
public interface ITrainingService {


    Training saveTraining(TrainingDto trainingDto);

    void deleteTraining(Integer id);

    Training updateTraining(TrainingDto trainingDto);

    Training getTrainingById(Integer id);

    Page<Training> findAllTrainingsBySeasonId(Pageable pageable, Integer seasonId);
}

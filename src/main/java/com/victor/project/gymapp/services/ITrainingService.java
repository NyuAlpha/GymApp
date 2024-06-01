package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.models.Training;

import dto.TrainingDto;

public interface ITrainingService extends CrudService{

    Page<Training> findAllTrainingsWithComment(Pageable pageable);
    Training saveTraining(TrainingDto trainingDto);
    void deleteTraining(Long id);
    Training updateTraining(Long id, TrainingDto trainingDto);
    TrainingDto getFullTrainingById(Long id);
    Page<Training> findAllTrainingsBySeasonId(Pageable pageable, Long seasonId);
}

package com.victor.project.gymapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.victor.project.gymapp.dto.TrainingDto;
import com.victor.project.gymapp.models.Training;

public interface ITrainingService extends CrudService {


    Training saveTraining(TrainingDto trainingDto);

    void deleteTraining(Integer id);

    Training updateTraining(TrainingDto trainingDto);

    Training getFullTrainingById(Integer id);

    Page<Training> findAllTrainingsBySeasonId(Pageable pageable, Integer seasonId);
}

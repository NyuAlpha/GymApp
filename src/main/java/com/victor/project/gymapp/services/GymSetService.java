package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.GymSetRepository;

@Service
public class GymSetService implements IGymSetService {

    private GymSetRepository gymSetRepository;
    private ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public void saveGymSet(GymSetDto gymSetDto) {

        GymSet gymSet = new GymSet();
        gymSet.setSetOrder(0);
        gymSet.setExercise(
                exerciseRepository.findById(gymSetDto.getExerciseId()).orElseThrow(NoSuchElementException::new));
        gymSet.setWeight(gymSetDto.getWeight());
        gymSet.setRepetitions(gymSetDto.getRepetitions());
        gymSet.setFailure(gymSetDto.getFailure());

        gymSetRepository.save(gymSet);

    }

    @Override
    @Transactional
    public void deleteGymSet(Long id) {
        gymSetRepository.deleteById(id);
    }

    @Override
    public GymSet updateGymSet(GymSetDto gymSetDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateGymSet'");
    }
}

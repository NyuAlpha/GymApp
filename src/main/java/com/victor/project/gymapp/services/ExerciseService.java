package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.ExerciseComment;
import com.victor.project.gymapp.models.ExerciseName;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.repositories.ExerciseNameRepository;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;

@Service
public class ExerciseService implements IExerciseService {

    private ExerciseRepository exerciseRepository;
    private ExerciseNameRepository exerciseNameRepository;
    private TrainingRepository trainingRepository;

    public ExerciseService(ExerciseRepository exerciseRepository,
            ExerciseNameRepository exerciseNameRepository,
            TrainingRepository trainingRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseNameRepository = exerciseNameRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public Exercise updateExercise(ExerciseDto exerciseDto) {

        // Obtenemos el ejercicio mediante id, si existe, se actualizan sus campos
        Exercise exercise = exerciseRepository.findById(exerciseDto.getId()).orElseThrow(NoSuchElementException::new);

        // Ahora se le asigna el nombre del ejercicio, como no puede estar vacio no hace
        // falta comprobar
        Optional<ExerciseName> optionalExerciseName = exerciseNameRepository.findByName(exerciseDto.getExerciseName());
        ExerciseName exerciseName;

        // Si no existe un ejercicio con ese nombre lo crea, si existe lo asigna
        if (optionalExerciseName.isEmpty())
            exerciseName = exerciseNameRepository.save(new ExerciseName(null, exerciseDto.getExerciseName()));
        else
            exerciseName = optionalExerciseName.get();

        // Se asigna el nombre al ejercicio
        exercise.setExerciseName(exerciseName);

        // Si el comentario no viene vacio entonces se crea un nuevo comentario.
        if (!exerciseDto.getExerciseComment().isBlank())
            exercise.setExerciseComment(new ExerciseComment(exerciseDto.getExerciseComment()));
        // Si el comentario viene vacio será null
        else
            exercise.setExerciseComment(null);

        exercise.setVariant(exerciseDto.getVariant());

        return exerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public ExerciseDto getFullExerciseById(Long id) {

        Optional<Exercise> optionalExercise = exerciseRepository.findByIdWithDetails(id);
        Exercise exercise = optionalExercise.orElseThrow(NoSuchElementException::new);

        return ExerciseDto.getDetailsDto(exercise);
    }

    @Override
    @Transactional
    public void saveExercise(ExerciseDto exerciseDto) {

        Exercise exercise = new Exercise();
        Training training = trainingRepository.findById(exerciseDto.getTrainingId()).orElseThrow();
        exercise.setTraining(training);

        // Si no existe un ejercicio con ese nombre lo crea, si existe lo asigna
        Optional<ExerciseName> optionalExerciseName = exerciseNameRepository.findByName(exerciseDto.getExerciseName());
        ExerciseName exerciseName = null;
        if (optionalExerciseName.isEmpty()) {
            exerciseName = exerciseNameRepository.save(new ExerciseName(null, exerciseDto.getExerciseName()));
        } else {
            exerciseName = optionalExerciseName.get();
        }
        // finalmente se asigna
        exercise.setExerciseName(exerciseName);

        // Si tiene un comentario y no está vacio entonces se crea un nuevo comentario.
        if (!exerciseDto.getExerciseComment().isBlank())
            exercise.setExerciseComment(new ExerciseComment(exerciseDto.getExerciseComment()));

        exercise.setVariant(exerciseDto.getVariant());

        exerciseRepository.save(exercise);

    }

    @Override
    @Transactional
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
}

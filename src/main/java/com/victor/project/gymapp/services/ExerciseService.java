package com.victor.project.gymapp.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (!exerciseDto.getExerciseComment().isBlank()){

            // Si ya tenía un comentario asignado simplemente se modifica el texto
            if (exercise.getExerciseComment() != null) {
                exercise.getExerciseComment().setComment(exerciseDto.getExerciseComment());
            } else {
                // Si no se crea un nuevo comentario
                exercise.setExerciseComment(new ExerciseComment(exerciseDto.getExerciseComment()));
            }
        }
        // Si el comentario viene vacio será null
        else
            exercise.setExerciseComment(null);



        exercise.setVariant(exerciseDto.getVariant());

        return exerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public Exercise getFullExerciseById(Integer id) {

        Optional<Exercise> optionalExercise = exerciseRepository.findByIdWithDetails(id);
        Exercise exercise = optionalExercise.orElseThrow(NoSuchElementException::new);

        return exercise;
    }


    

    @Override
    @Transactional
    public Exercise saveExercise(ExerciseDto exerciseDto) {

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



        //Por último, se calcula cuantos ejercicios hay en el entrenamiento para darle una posición al nuevo
        Byte count = exerciseRepository.countByTrainingId(exerciseDto.getTrainingId());
        exercise.setExerciseOrder(count);//Si tiene 4, la posición del último es la 3, por eso se pone el mismo que count

        return exerciseRepository.save(exercise);

    }

    @Override
    @Transactional
    public void deleteExercise(Integer id) {
        exerciseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void down(Integer exerciseId, Integer trainingId) {

        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(NoSuchElementException::new);
        //obtenemos su posición
        byte order = exercise.getExerciseOrder();
        
        //Buscamos un anterior
        Optional<Exercise> optionaPreviousExercise = exerciseRepository.findByTrainingIdAndExerciseOrder(trainingId,(byte)(order -1));

        //Si existe un anterior entonces peude bajar un nivel
        if(optionaPreviousExercise.isPresent()){

            Exercise previousExercise = optionaPreviousExercise.get();

            //Intercambio de orden
            exercise.setExerciseOrder(previousExercise.getExerciseOrder());
            previousExercise.setExerciseOrder(order);

            exerciseRepository.saveAll(Arrays.asList(exercise,previousExercise));
        }
        
    }

    @Override
    @Transactional
    public void up(Integer exerciseId, Integer trainingId) {

        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(NoSuchElementException::new);
        //obtenemos su posición
        byte order = exercise.getExerciseOrder();
        
        //Buscamos un posterior
        Optional<Exercise> optionaNextExercise = exerciseRepository.findByTrainingIdAndExerciseOrder(trainingId,(byte)(order+1));

        //Si existe un posterior entonces se puede subir un nivel
        if(optionaNextExercise.isPresent()){
            
            Exercise nextExercise = optionaNextExercise.get();

            //Intercambio de orden
            exercise.setExerciseOrder(nextExercise.getExerciseOrder());
            nextExercise.setExerciseOrder(order);

            exerciseRepository.saveAll(Arrays.asList(exercise,nextExercise));
        }
        
    }



    public ResponseEntity<?> getExercise(Integer exerciseId){

        Optional<Exercise> optionalExercise =  exerciseRepository.findById(exerciseId);

        if(optionalExercise.isPresent()){
            return ResponseEntity.ok().body(optionalExercise.get().getDto());
        }
        else{
            Map<String,String> errors = new HashMap<>();
            errors.put("error","not_found");
            errors.put("message","Recurso no encontrado");
            errors.put("date",LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errors);
        }
    }

    
}

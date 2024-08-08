package com.victor.project.gymapp.services;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.ExerciseName;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.repositories.ExerciseNameRepository;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;

import lombok.AllArgsConstructor;

/*
 * Servicio para manipulación de ejercicios
 */
@Service
@AllArgsConstructor
public class ExerciseService implements IExerciseService {



    //Repositorios
    private ExerciseRepository exerciseRepository;
    private ExerciseNameRepository exerciseNameRepository;
    private TrainingRepository trainingRepository;








    /*
     * Procesa y guarda el ejercicio en la base de datos, usa el dto
     * para establecer los campos, luego lo devuelve
     */
    @Override
    @Transactional
    public Exercise saveExercise(ExerciseDto exerciseDto) {

        //Se crea un nuevo ejercicio en base al dto
        Exercise exercise = new Exercise(exerciseDto);
        
        //Se le da un entrenamiento padre en base al id padre que contenga
        Training training = trainingRepository.findById(exerciseDto.getTrainingId()).orElseThrow(NoSuchElementException::new);
        exercise.setTraining(training);

        //Asignamos un nombre de ejercicio
        asignName(exercise, exerciseDto.getExerciseName());


        //Por último, se calcula cuantos ejercicios hay en el entrenamiento para darle una posición al nuevo
        Byte count = exerciseRepository.countByTrainingId(exerciseDto.getTrainingId());
        exercise.setExerciseOrder(count);//Si tiene 4, la posición del último es la 3, por eso se pone el mismo que count

        return exerciseRepository.save(exercise);

    }








    /*
     * Regresa un ejercicio si lo encuentra, lo busca por su id
     */
    @Override
    @Transactional
    public Exercise getExerciseById(Integer id) {

        Optional<Exercise> optionalExercise = exerciseRepository.findByIdWithDetails(id);
        Exercise exercise = optionalExercise.orElseThrow(NoSuchElementException::new);

        return exercise;
    }









    /*
     * Actualiza un ejercicio con los datos enviados en el dto,
     * luego lo devuelve
     */
    @Override
    @Transactional
    public Exercise updateExercise(ExerciseDto exerciseDto) {

        // Obtenemos el ejercicio mediante id, si existe, se actualizan sus campos
        Exercise exercise = exerciseRepository.findById(exerciseDto.getId()).orElseThrow(NoSuchElementException::new);
        //Se actualizan sus campos mediante el dto
        exercise.update(exerciseDto);

        //Reasignamos un nombre de ejercicio
        asignName(exercise, exerciseDto.getExerciseName());

        return exerciseRepository.save(exercise);
    }













    /*
     * Borra el ejercicio por el id
     */
    @Override
    @Transactional
    public void deleteExercise(Integer id) {
        exerciseRepository.deleteById(id);
    }











    /*
     * Baja un ejercicio de posición en la lista de ejercicios
     */
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











    /*
     * sube un ejercicio de posición en la lista de ejercicios
     */
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








    /*
     * Obtiene nombres de ejercicios que empiezan por el token
     */
    @Override
    @Transactional
    public List<String> getExerciseNames(String token) {
        return exerciseNameRepository.findAllLikeName(token);
    }



    /* 
     * Se asigna el nombre, pero primero se comprueba si existe, si no existe se crea uno nuevo
     * y se persiste antes de asignarse
     */
    @Transactional
    private void asignName(Exercise exercise ,String name){

        Optional<ExerciseName> optionalExerciseName = exerciseNameRepository.findByName(name);

        if(optionalExerciseName.isPresent()){
            exercise.setExerciseName(optionalExerciseName.get());
        }else{
            ExerciseName en = exerciseNameRepository.save(new ExerciseName(name));
            exercise.setExerciseName(en);
        }
    }

    
}

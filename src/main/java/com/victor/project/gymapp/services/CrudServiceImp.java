package com.victor.project.gymapp.services;


import java.net.http.HttpResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.ExerciseComment;
import com.victor.project.gymapp.models.ExerciseName;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.models.TrainingComment;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.ExerciseNameRepository;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.SetRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;
import com.victor.project.gymapp.repositories.UserDetailsRepository;
import com.victor.project.gymapp.repositories.UserRepository;

import dto.ExerciseDto;
import dto.TrainingDto;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@Service
@Primary
public class CrudServiceImp implements CrudService{


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseNameRepository exerciseNameRepository;
    @Autowired
    private SetRepository setRepository;


    @PostConstruct
    public void run(){
        //insertData();
    }


    @Override
    @Transactional
    public Page<Training> findAllTrainingsWithComment(Pageable pageable) {
        
        return trainingRepository.findAllWithCommentByOrderByDateAsc(pageable);
    }

    /*
     * Crea un entrenamiento para el usuario administrador, lo guarda en la base de datos y lo retorna
     */
    @Override
    @Transactional
    public Training saveTraining(TrainingDto trainingDto){

        //Código temporal hasta terminar el login, todos los entrenamientos serán de este usuario
        Optional<User> optionalUser = userRepository.findByUsername("superadmin");

        Training training = new Training();
        training.setUser(optionalUser.orElseThrow());
        training.setDate(trainingDto.getDate());

        //Si tiene comentario se le asigna
        if(!trainingDto.getTrainingComment().isEmpty()){
            TrainingComment trainingComment = new TrainingComment();
            trainingComment.setComment(trainingDto.getTrainingComment());
            training.setTrainingComment(trainingComment);
        }
        return trainingRepository.save(training);
    }

    /*
     * Elimina un entrenamiento basandose en el id
     */
    @Override
    @Transactional
    public void deleteTraining(Long id){
        trainingRepository.deleteById(id);
    }

    /*
     * Busca el entrenamiento por id
     */
    @Override
    @Transactional
    public TrainingDto getFullTrainingById(Long id){
        //Busca por id el entrenamiento, incluyendo sus ejercicios y series
        Optional<Training> optionalTraining = trainingRepository.findByIdWithDetails(id);
        //Si no se encuentra se lanza la excepción
        Training training = optionalTraining.orElseThrow(NoSuchElementException::new);
        //Ahora puede devolver el entrenamiento y sus detalles en forma de dto
        return TrainingDto.getDetailsDto(training);
    }

    @Override
    @Transactional
    public Training updateTraining(@Valid TrainingDto trainingDto){

        //Conseguimos el entrenamiendo por id con los detalles básicos
        Optional<Training> optionalTraining = trainingRepository.findByIdWithDetails(trainingDto.getId());

        //Si no se encuentra se lanza la excepción
        Training training = optionalTraining.orElseThrow(NoSuchElementException::new);

        training.setDate(trainingDto.getDate());

        //Si tiene comentario se le asigna
        if(!trainingDto.getTrainingComment().isEmpty()){
            
            //Si ya existe un comentario lo reemplaza directamente
            if(training.getTrainingComment() != null){
                //Actualiza el comentario que tenía previamente
                training.getTrainingComment().setComment(trainingDto.getTrainingComment());
            }
            else{
                //Si no hay comentario previo se crea uno nuevo
                TrainingComment trainingComment = new TrainingComment();
                trainingComment.setComment(trainingDto.getTrainingComment());
                training.setTrainingComment(trainingComment);
            }
        }
        //Se guarda y se devuelve en forma de DTO
        return trainingRepository.save(training);
    }


    @Override
    @Transactional
    public void saveExercise(ExerciseDto exerciseDto) {
        
        Exercise exercise = new Exercise();
        System.out.println("ID------>>>>>>>> " + exerciseDto.getId() + "(" + exerciseDto.getTrainingId() + "tr)");
        Training training = trainingRepository.findById(exerciseDto.getTrainingId()).orElseThrow();
        exercise.setTraining(training);

        //Si no existe un ejercicio con ese nombre lo crea, si existe lo asigna
        Optional<ExerciseName> optionalExerciseName = exerciseNameRepository.findByName(exerciseDto.getExerciseName());
        ExerciseName exerciseName = null;
        if(optionalExerciseName.isEmpty()){
            exerciseName = exerciseNameRepository.save(new ExerciseName(null, exerciseDto.getExerciseName()));
        }else{
            exerciseName = optionalExerciseName.get();
        }
        //finalmente se asigna
        exercise.setExerciseName(exerciseName);

        if(!exerciseDto.getExerciseComment().isEmpty()){
            ExerciseComment exerciseComment = new ExerciseComment();
            exerciseComment.setComment(exerciseDto.getExerciseComment());
            exercise.setExerciseComment(exerciseComment);
        }

        exercise.setVariant(exerciseDto.getVariant());
        
        exerciseRepository.save(exercise);

    }


    @Override
    @Transactional
    public Optional<Exercise> getFullExerciseById(Long id) {
        return exerciseRepository.findByIdWithDetails(id);
    }


    


    
}

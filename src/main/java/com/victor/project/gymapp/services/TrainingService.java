package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.models.TrainingComment;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.TrainingRepository;
import com.victor.project.gymapp.repositories.UserRepository;

import dto.TrainingDto;

@Service
public class TrainingService implements ITrainingService{


    private TrainingRepository trainingRepository;
    private UserRepository userRepository;

    

    public TrainingService(TrainingRepository trainingRepository, UserRepository userRepository) {
        this.trainingRepository = trainingRepository;
        this.userRepository = userRepository;
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
        training.setTitle(trainingDto.getTitle());

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
    public Training updateTraining(Long id,TrainingDto trainingDto){

        //Conseguimos el entrenamiendo por id con los detalles básicos
        Optional<Training> optionalTraining = trainingRepository.findByIdWithDetails(id);

        //Si no se encuentra se lanza la excepción
        Training training = optionalTraining.orElseThrow(NoSuchElementException::new);

        training.setDate(trainingDto.getDate());
        training.setTitle(trainingDto.getTitle());

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
    public Page<Training> findAllTrainingsBySeasonId(Pageable pageable, Long seasonId) {
        return trainingRepository.findBySeasonId(pageable,seasonId);
    }


}

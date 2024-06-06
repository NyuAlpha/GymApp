package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.TrainingDto;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.models.TrainingComment;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.TrainingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TrainingService implements ITrainingService {

    private TrainingRepository trainingRepository;
    private SeasonRepository seasonRepository;



    /*
     * Crea un entrenamiento para el usuario administrador, lo guarda en la base de
     * datos y lo retorna
     */
    @Override
    @Transactional
    public Training saveTraining(TrainingDto trainingDto) {

        // El id debe estar vacio cuando creamos el registro por primera vez, por lo que nos aseguramos.
        trainingDto.setId(null);
        //Ahora volcamos el dto en la entidad para la bbdd
        Training training = new Training(trainingDto);
        //Se le asigna la temporada mediante el id, se presupone que ya se ha verificado dicha temporada.
        training.setSeason(seasonRepository.findById(trainingDto.getSeasonId()).orElseThrow(NoSuchElementException::new));

        //Se persiste y se devuelve
        return trainingRepository.save(training);
    }

    /*
     * Elimina un entrenamiento basandose en el id
     */
    @Override
    @Transactional
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

    /*
     * Busca el entrenamiento por id
     */
    @Override
    @Transactional
    public Training getFullTrainingById(Long id) {
        // Busca por id el entrenamiento, incluyendo sus ejercicios y series
        Optional<Training> optionalTraining = trainingRepository.findByIdWithDetails(id);
        // Si no se encuentra se lanza la excepción
        Training training = optionalTraining.orElseThrow(NoSuchElementException::new);
        // Ahora puede devolver el entrenamiento y sus detalles en forma de dto
        return training;
    }

    @Override
    @Transactional
    public Training updateTraining(TrainingDto trainingDto) {

        // Conseguimos el entrenamiendo por id con los detalles básicos
        Training training = trainingRepository.findByIdWithDetails(trainingDto.getId()).orElseThrow(NoSuchElementException::new);

        training.setDate(trainingDto.getDate());
        training.setTitle(trainingDto.getTitle());

        // Si tiene comentario se le asigna
        if (!trainingDto.getTrainingComment().isEmpty()) {

            // Si ya existe un comentario lo reemplaza directamente
            if (training.getTrainingComment() != null) {
                // Actualiza el comentario que tenía previamente
                training.getTrainingComment().setComment(trainingDto.getTrainingComment());
            } else {
                // Si no hay comentario previo se crea uno nuevo
                TrainingComment trainingComment = new TrainingComment();
                trainingComment.setComment(trainingDto.getTrainingComment());
                training.setTrainingComment(trainingComment);
            }
        }
        // Se guarda y se devuelve en forma de DTO
        return trainingRepository.save(training);
    }

    @Override
    @Transactional
    public Page<Training> findAllTrainingsBySeasonId(Pageable pageable, Long seasonId) {
        return trainingRepository.findBySeasonId(pageable, seasonId);
    }

}

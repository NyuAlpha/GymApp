package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.expression.Arrays;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.GymSetRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GymSetService implements IGymSetService {

    private GymSetRepository gymSetRepository;
    private ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public void saveGymSet(GymSetDto gymSetDto) {

        GymSet gymSet = new GymSet();

        //Se calcula la posición de la serie y se le asigna su entrenamiento, ya que ambos son la primary key
        Byte count = gymSetRepository.countByExerciseId(gymSetDto.getExerciseId());
        Exercise exercise = exerciseRepository.findById(gymSetDto.getExerciseId()).orElseThrow(NoSuchElementException::new);
        gymSet.setId(exercise,count);//Si tiene 4, la posición del último es la 3, por eso se pone el mismo que count

        //Resto de campos
            
        gymSet.setWeight(gymSetDto.getWeight());
        Byte reps = (gymSetDto.getRepetitions()==null)? null:gymSetDto.getRepetitions().byteValue();
        gymSet.setRepetitions(reps);
        Byte times = (gymSetDto.getRepetitions()==null)? 1:gymSetDto.getTimesRepeated().byteValue();
        gymSet.setTimesRepeated(times);
        gymSet.setFailure(gymSetDto.getFailure());

       
        gymSetRepository.save(gymSet);

    }

    @Override
    @Transactional
    public void deleteGymSet(Integer exerciseId, Byte setOrder) {
        gymSetRepository.deleteByExerciseIdAndSetOrder(exerciseId,setOrder);
    }

    @Override
    @Transactional
    public void updateGymSet(GymSetDto gymSetDto) {
        GymSet gymSet = gymSetRepository.findByExerciseIdAndSetOrder(gymSetDto.getExerciseId(),gymSetDto.getSetOrder())
                            .orElseThrow(NoSuchElementException::new);
            
        gymSet.setWeight(gymSetDto.getWeight());
        Byte reps = (gymSetDto.getRepetitions()==null)? null:gymSetDto.getRepetitions().byteValue();
        gymSet.setRepetitions(reps);
        Byte times = (gymSetDto.getRepetitions()==null)? null:gymSetDto.getTimesRepeated().byteValue();
        gymSet.setTimesRepeated(times);
        gymSet.setFailure(gymSetDto.getFailure());

       
        gymSetRepository.save(gymSet);
    }

    @Override
    @Transactional
    public void down(Integer exerciseId, Byte setOrder) {

        GymSet gymSet = gymSetRepository.findByExerciseIdAndSetOrder(exerciseId,setOrder).orElseThrow(NoSuchElementException::new);
        //obtenemos su posición
        byte order = gymSet.getId().getSetOrder();
        
        //Buscamos un anterior
        Optional<GymSet> optionaPreviousGymSet = gymSetRepository.findByExerciseIdAndSetOrder(exerciseId,(byte)(order-1));

        if(optionaPreviousGymSet.isPresent()){

            // Utilizar consultas nativas para actualizar set_order
            //La serie que va a subir se mueve a la posición -1 temporalmente y se guarda
            gymSetRepository.updateSetOrder(exerciseId, (byte) (order - 1), (byte) -1);
            //Ahora que la anterior ya no ocupa dicha posición, puede bajar
            gymSetRepository.updateSetOrder(exerciseId, order, (byte) (order - 1));
            //La anterior, la cual ha estado en -1 temporalmente, ocupará su lugar ascendiendo
            gymSetRepository.updateSetOrder(exerciseId, (byte) -1, order);
        }
    }

    @Override
    @Transactional
    public void up(Integer exerciseId, Byte setOrder) {
        GymSet gymSet = gymSetRepository.findByExerciseIdAndSetOrder(exerciseId,setOrder).orElseThrow(NoSuchElementException::new);
        //obtenemos su posición
        byte order = gymSet.getId().getSetOrder();
        
        //Buscamos un anterior
        Optional<GymSet> optionaPreviousGymSet = gymSetRepository.findByExerciseIdAndSetOrder(exerciseId,(byte)(order+1));

        if(optionaPreviousGymSet.isPresent()){

            // Utilizar consultas nativas para actualizar set_order
            //La serie que va a subir se mueve a la posición -1 temporalmente y se guarda
            gymSetRepository.updateSetOrder(exerciseId, (byte) (order + 1), (byte) -1);
            //Ahora que la anterior ya no ocupa dicha posición, puede bajar
            gymSetRepository.updateSetOrder(exerciseId, order, (byte) (order + 1));
            //La anterior, la cual ha estado en -1 temporalmente, ocupará su lugar ascendiendo
            gymSetRepository.updateSetOrder(exerciseId, (byte) -1, order);
        }
        
    }


    
}

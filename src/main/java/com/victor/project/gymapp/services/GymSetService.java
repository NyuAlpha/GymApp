package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.repositories.ExerciseRepository;
import com.victor.project.gymapp.repositories.GymSetRepository;

import lombok.AllArgsConstructor;


/*
 * Servicio para procesar datos de Series de ejercicios
 */
@Service
@AllArgsConstructor
public class GymSetService implements IGymSetService {

    private GymSetRepository gymSetRepository;
    private ExerciseRepository exerciseRepository;




    /*
     * Crea en base al dto un ejercicio y lo guarda
     */
    @Override
    @Transactional
    public void saveGymSet(GymSetDto gymSetDto) {


        //Crea una serie en base al dto con los campos simples
        GymSet gymSet = new GymSet(gymSetDto);

        

        //Asigna su primary key
        //Se calcula la posición de la serie y se le asigna su entrenamiento, ya que ambos son la primary key
        Byte count = gymSetRepository.countByExerciseId(gymSetDto.getExerciseId());
        System.out.println("count = " + count);
        Exercise exercise = exerciseRepository.findById(gymSetDto.getExerciseId()).orElseThrow(NoSuchElementException::new);
        gymSet.setId(exercise,count);//Si tiene 4, la posición del último es la 3, por eso se pone el mismo que count

        //Se guarda
        gymSetRepository.save(gymSet);

    }





    /*
     * Borra la serie que pertenece al ejercicio y tiene la posición de los argumentos enviados
     */
    @Override
    @Transactional
    public void deleteGymSet(Integer exerciseId, Byte setOrder) {

        gymSetRepository.deleteByExerciseIdAndSetOrder(exerciseId,setOrder);

        //Ahora se debe reestablecer el orden restando 1 al setOrder de cada serie posterior a la eliminada.
        gymSetRepository.updateSetOrder(exerciseId, setOrder);

    }





    /*
     * Actualiza una serie en base a los datos del dto
     */
    @Override
    @Transactional
    public void updateGymSet(GymSetDto gymSetDto) {

        GymSet gymSet = gymSetRepository.findByExerciseIdAndSetOrder(gymSetDto.getExerciseId(),gymSetDto.getSetOrder())
                            .orElseThrow(NoSuchElementException::new);
        
        //Se actualizan sus campos simples en base al dto
        gymSet.update(gymSetDto);
       
        gymSetRepository.save(gymSet);
    }






    
    /*
    * Asciende una posición (orden) la serie pasada como parámetro en base a su ejercicio y orden
    */
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





    /*
    * Desciende una posición (orden) la serie pasada como parámetro en base a su ejercicio y orden
    */
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


    /*
     * Retorna la última serie si existe, retorna una vacia si no existe
     */
    public GymSet getLastGymSet(Integer exerciseId){

        Optional<GymSet> optionalGymSet = gymSetRepository.findLastByExerciseId(exerciseId);
        if(optionalGymSet.isPresent()){
            System.out.println(" El id es...." + optionalGymSet.get().getId().getSetOrder());
            return optionalGymSet.get();
        }
        
        System.out.println("\n\nNo encontrado...*********************************\n\n");
        return null;
    }
}

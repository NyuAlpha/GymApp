package dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.GymSet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * Clase dto para llevar y traer los datos de un ejercicio a las vistas.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ExerciseDto {

    private Long id;

    private Long trainingId;

    private String exerciseName;//Nombre del ejercicio

    private String variant;//Variante del ejercicio

    private String exerciseComment;//comentario asociado a un ejercicio

    private Set<SetDto> gymSetsDto; //Las series Dto de cada ejercicio irán incorporadas en esta lista

    public static ExerciseDto getsimpleDto(Exercise exercise){
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setId(exercise.getId());
        //No comprobamos si tiene nombre de ejercicio puesto que no puede ser null bajo ningún concepto
        exerciseDto.setExerciseName(exercise.getExerciseName().getName());
        exerciseDto.setVariant(exercise.getVariant());
        //Comprobamos si hay un comentario asociado al ejercicio
        if(exercise.getExerciseComment() != null){
             exerciseDto.setExerciseComment(exercise.getExerciseComment().getComment());
         }
        return exerciseDto;
    }

    public static ExerciseDto getDetailsDto(Exercise exercise){
        ExerciseDto exerciseDto = getsimpleDto(exercise);

        //Conseguimos el id de su entrenamiento para tener la referencia al entrenamiento en caso de querer volver atrás en la vista.
        exerciseDto.setTrainingId(exercise.getTraining().getId());

        //Ahora cargamos las series de este ejercicio en caso de que existan
        Set<GymSet> sets = exercise.getGymSets();
        if(!sets.isEmpty()){
            //Se transforma la lista de series a la lista de series dto
            Set<SetDto> setsDto = sets.stream()
                .map(set-> SetDto.getsimpleDto(set))
                .collect(Collectors.toSet());
            //Finalmente se lo pasamos a la instacia de ejercicio dto
            exerciseDto.setGymSetsDto(setsDto);
        }

        return exerciseDto;
    }

}
